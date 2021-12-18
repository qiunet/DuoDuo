package org.qiunet.game.test.robot;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.IPersistConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebSocketClient;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.param.adapter.message.StatusTipsResponse;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.game.test.bt.RobotBehaviorBuilderManager;
import org.qiunet.game.test.response.IStatusTipsHandler;
import org.qiunet.game.test.response.ResponseMapping;
import org.qiunet.game.test.robot.action.BaseRobotAction;
import org.qiunet.game.test.server.IServer;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.string.ToString;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 17/12/9
 */
abstract class RobotFunc extends MessageHandler<Robot> implements IMessageHandler<Robot> , IArgsContainer {
	protected static final Logger logger = LoggerType.DUODUO_GAME_TEST.getLogger();
	/**
	 * 存储各种数据的一个容器.
	 */
	protected final ArgsContainer container = new ArgsContainer();
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
	private DFuture<?> tickFuture;
	/**
	 *
	 */
	private final BehaviorRootTree behaviorRootTree;
	/**
	 * robot 心跳时间
	 */
	private final int tickMillis;

	protected RobotFunc(int tickMillis) {
		this.tickMillis = tickMillis;
		this.behaviorRootTree = RobotBehaviorBuilderManager.instance.buildRootExecutor((this));
		this.tickFuture = this.scheduleMessage(h -> this.tickRun(), MathUtil.random(20, 200), TimeUnit.MILLISECONDS);
	}

	/**
	 * 心跳. 执行玩run. 延迟500 执行下次心跳. 不是等时的
	 */
	private void tickRun(){
		this.behaviorRootTree.tick();
		this.tickFuture = this.scheduleMessage(h1 -> this.tickRun(), tickMillis, TimeUnit.MILLISECONDS);
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
							.getSender();
				default:
					throw new CustomException("Type [{}] is not support", server.getType());
			}
		});
	}

	private class PersistConnResponseTrigger implements IPersistConnResponseTrigger {

		@Override
		public void response(DSession session, MessageContent data) {
			RobotFunc.this.addMessage(h -> response0(session, data));
		}

		private void response0(DSession session, MessageContent data) {
			if (data.getProtocolId() == IProtocolId.System.ERROR_STATUS_TIPS_RESP) {
				this.handlerStatus(data);
				return;
			}

			Method method = ResponseMapping.getResponseMethodByID(data.getProtocolId());
			if (method == null) {
				session.close(CloseCause.LOGOUT);
				brokeRobot("Response ID ["+data.getProtocolId()+"] not define!");
				return;
			}

			Class<?> declaringClass = method.getDeclaringClass();
			IBehaviorAction action = actionClzMapping.get(declaringClass);
			Class<? extends IChannelData> protocolClass = ChannelDataMapping.protocolClass(data.getProtocolId());
			IChannelData realData = ProtobufDataManager.decode(protocolClass, data.bytes());

			try {
				method.invoke(action, realData);
			} catch (Exception e) {
				throw new CustomException(e, "response exception!");
			}
		}

		private void handlerStatus(MessageContent data) {
			StatusTipsResponse response = ProtobufDataManager.decode(StatusTipsResponse.class, data.bytes());
			logger.info("[{}] <<< {}", RobotFunc.this.getIdentity(), ToString.toString(response));
			Method method = ResponseMapping.getStatusMethodByID(response.getStatus());
			if (method == null) {
				brokeRobot("status ID ["+response.getStatus()+"] handler not define!");
				return;
			}

			Class<?> declaringClass = method.getDeclaringClass();
			IBehaviorAction action = actionClzMapping.get(declaringClass);
			((IStatusTipsHandler) action).statusHandler(response);
		}
	}

	/**
	 * 断开所有session . 打印message
	 * @param message
	 */
	public void brokeRobot(String message) {
		clients.forEach((key, session) -> session.close(CloseCause.LOGOUT));
		LoggerType.DUODUO_GAME_TEST.error(message);
	}
	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return container.getArgument(key, computeIfAbsent);
	}

	public void registerAction(BaseRobotAction action) {
		this.actionClzMapping.put(action.getClass(), action);
	}
}
