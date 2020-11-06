package org.qiunet.test.robot;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.test.response.ILongConnResponse;
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
	private BaseRobotFunc.LongConnResponseTrigger trigger = new BaseRobotFunc.LongConnResponseTrigger();
	/***
	 * 长连接的session map
	 */
	private Map<String, ILongConnClient> clients = Maps.newConcurrentMap();

	private IRobot robot;
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
	public ILongConnClient getLongConnClient(IServer server) {
		return clients.computeIfAbsent(server.name(), serverName -> {
			switch (server.getType()) {
				case WEB_SOCKET:
					return NettyWebsocketClient.create(((WebSocketClientParams) server.getClientConfig()), trigger);
				case TCP:
					return NettyTcpClient.create((TcpClientParams) server.getClientConfig(), trigger).connect(server.host(), server.port());
				default:
					throw new CustomException("Type [{}] is not support", server.getType());
			}
		});
	}

	private class LongConnResponseTrigger implements ILongConnResponseTrigger {

		@Override
		public void response(DSession session, MessageContent data) {
			ILongConnResponse response = ResponseMapping.instance.getResponse(data.getProtocolId());
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
