package org.qiunet.game.test.robot;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataMapping;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebSocketClient;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.game.test.behavior.action.IBehaviorAction;
import org.qiunet.game.test.behavior.action.IBehaviorType;
import org.qiunet.game.test.response.ResponseMapping;
import org.qiunet.game.test.server.IServer;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 17/12/9
 */
abstract class RobotFunc extends MessageHandler<Robot> implements IMessageHandler<Robot> {
	/**
	 * 每个类型对应的action
	 */
	private final Map<Enum<? extends IBehaviorType>, IBehaviorAction> actionMapping = Maps.newHashMap();
	/**
	 * class 对应实例
	 */
	private final Map<Class<? extends IBehaviorAction>, IBehaviorAction> actionClzMapping = Maps.newHashMap();
	/**
	 * 机器人接受相应的类.
	 */
	private final PersistConnResponseTrigger trigger = new PersistConnResponseTrigger();
	/***
	 * 长连接的session map
	 */
	private final Map<String, DSession> clients = Maps.newConcurrentMap();
	/**
	 * 心跳future
	 */
	private final Future<?> tickFuture;

	protected RobotFunc() {
		this.tickFuture = this.scheduleAtFixedRate("Robot.tick", h -> this.tick(), 10, 500, TimeUnit.MILLISECONDS);
	}

	/**
	 * 心跳
	 */
	private void tick() {

	}

	public Future<?> getTickFuture() {
		return tickFuture;
	}

	public DSession getPersistConnClient(IServer server) {
		return clients.computeIfAbsent(server.name(), serverName -> {
			switch (server.getType()) {
				case WS:
					return NettyWebSocketClient.create(((WebSocketClientParams) server.getClientConfig()), trigger);
				case TCP:
					return NettyTcpClient.create((TcpClientParams) server.getClientConfig(), trigger)
							.connect(server.host(), server.port())
							.getSession();
				default:
					throw new CustomException("Type [{}] is not support", server.getType());
			}
		});
	}

	private class PersistConnResponseTrigger implements IPersistConnResponseTrigger {

		@Override
		public void response(DSession session, MessageContent data) {
			Method method = ResponseMapping.getResponseMethodByID(data.getProtocolId());
			if (method == null) {
				session.close(CloseCause.LOGOUT);
				brokeRobot("Response ID ["+data.getProtocolId()+"] not define!");
				return;
			}

			Class<?> declaringClass = method.getDeclaringClass();
			IBehaviorAction action = actionClzMapping.get(declaringClass);
			Class<? extends IpbChannelData> protocolClass = PbChannelDataMapping.protocolClass(data.getProtocolId());
			IpbChannelData realData = ProtobufDataManager.decode(protocolClass, data.bytes());
			try {
				method.invoke(action, realData);
			} catch (Exception e) {
				throw new CustomException(e, "response exception!");
			}
		}
	}

	public void brokeRobot(String message) {
		LoggerType.DUODUO_GAME_TEST.error(message);
	}
}
