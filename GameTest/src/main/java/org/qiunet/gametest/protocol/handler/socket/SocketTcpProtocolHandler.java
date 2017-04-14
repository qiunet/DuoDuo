package org.qiunet.gametest.protocol.handler.socket;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.qiunet.enums.ProtocolType;
import org.qiunet.gametest.protocol.base.IProtocol;
import org.qiunet.gametest.protocol.base.LoginProtocol;
import org.qiunet.gametest.protocol.handler.IProtocolHandler;
import org.qiunet.gametest.robot.IRobot;
import org.qiunet.gametest.robot.TcpRobot;

import java.net.InetSocketAddress;

/**
 * @author qiunet
 *         Created on 16/12/14 16:51.
 */
public abstract class SocketTcpProtocolHandler implements IProtocolHandler {
	private Logger logger = Logger.getLogger(getClass());
	private InetSocketAddress socketAddress;
	private NioSocketConnector connector;

	protected SocketTcpProtocolHandler(ProtocolCodecFactory protocolCodecFactory, InetSocketAddress socketAddress) {
		this.socketAddress = socketAddress;

		connector.setHandler(MinaHandler.getInstance());
		connector.setConnectTimeoutMillis(5000);
		connector.setDefaultRemoteAddress(socketAddress);
		connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(protocolCodecFactory));
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
	}


	@Override
	public boolean login(IRobot robot) {
		LoginProtocol protocol = robot.login(getType());
		ConnectFuture cf = connector.connect();
		cf.awaitUninterruptibly();
		if (cf.isDone() && cf.isConnected()) {
			((TcpRobot)robot).setTcpIoSession(cf.getSession());
			return handler(protocol, robot);
		}else {
			return false;
		}
	}

	@Override
	public boolean handler(IProtocol protocol, IRobot robot) {
		Object object = protocol.request(robot);
		IoSession session = ((TcpRobot)robot).getTcpSession();
		session.write(object);
		return true;
	}

	@Override
	public ProtocolType getType() {
		return ProtocolType.SOCKET_TCP;
	}
}
