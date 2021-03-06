package org.qiunet.test.robot;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebSocketClient;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.test.response.IPersistConnResponse;
import org.qiunet.test.response.annotation.support.ResponseMapping;
import org.qiunet.test.robot.init.IRobotInitInfo;
import org.qiunet.test.server.IServer;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/12/9
 */
abstract class BaseRobotFunc<Info extends IRobotInitInfo> implements IRobot<Info> {
	private final PersistConnResponseTrigger trigger = new PersistConnResponseTrigger();
	/***
	 * 长连接的session map
	 */
	private final Map<String, IChannelMessageSender> clients = Maps.newConcurrentMap();

	private final IRobot robot;
	public BaseRobotFunc(){
		robot = this;
	}

	private Thread currThread;
	private int parkResponseId;
	@Override
	public void parkForResponseID(int parkResponseId) {
		this.parkResponseId = parkResponseId;
		currThread = Thread.currentThread();
		LockSupport.park();
	}

	@Override
	public IChannelMessageSender getPersistConnClient(IServer server) {
		return clients.computeIfAbsent(server.name(), serverName -> {
			switch (server.getType()) {
				case WS:
					return NettyWebSocketClient.create(((WebSocketClientParams) server.getClientConfig()), trigger);
				case TCP:
					return NettyTcpClient.create((TcpClientParams) server.getClientConfig(), trigger).connect(server.host(), server.port());
				default:
					throw new CustomException("Type [{}] is not support", server.getType());
			}
		});
	}

	private class PersistConnResponseTrigger implements IPersistConnResponseTrigger {

		@Override
		public void response(DSession session, MessageContent data) {
			IPersistConnResponse response = ResponseMapping.instance.getResponse(data.getProtocolId());
			if (response == null) {
				session.close(CloseCause.LOGOUT);
				robot.brokeRobot("Response ID ["+data.getProtocolId()+"] not define!");
				LockSupport.unpark(currThread);
				return;
			}
			response.response(robot, data);

			if (data.getProtocolId() == BaseRobotFunc.this.parkResponseId) {
				BaseRobotFunc.this.parkResponseId = 0;
				LockSupport.unpark(currThread);
			}
		}
	}
}
