package org.qiunet.test.robot;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebsocketClient;
import org.qiunet.test.response.ILongConnResponse;
import org.qiunet.test.response.annotation.support.ResponseMapping;
import org.qiunet.test.robot.init.IRobotInitInfo;
import org.qiunet.test.server.IServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
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
	private Map<String, ILongConnClient> clients = new HashMap<>();

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
		if (clients.containsKey(server.getName())) return clients.get(server.getName());
		ILongConnClient connClient = null;
		switch (server.getType()) {
			case WEB_SOCKET:
				connClient = new NettyWebsocketClient(server.uri(), trigger);
				break;
			case TCP:
				connClient = new NettyTcpClient(new InetSocketAddress(server.uri().getHost(), server.uri().getPort()), trigger);
				break;
		}
		clients.put(server.getName(), connClient);
		return clients.get(server.getName());
	}

	private class LongConnResponseTrigger implements ILongConnResponseTrigger {

		@Override
		public void response(MessageContent data) {
			ILongConnResponse response = ResponseMapping.getInstance().getResponse(data.getProtocolId());
			response.response(robot, data);

			if (data.getProtocolId() == BaseRobotFunc.this.parkResponseId) {
				BaseRobotFunc.this.parkResponseId = 0;
				LockSupport.unpark(currThread);
			}
		}
	}
}
