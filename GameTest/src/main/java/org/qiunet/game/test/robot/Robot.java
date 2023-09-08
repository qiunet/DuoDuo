package org.qiunet.game.test.robot;

import com.google.common.collect.Maps;
import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.IRobot;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ClientSession;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.client.kcp.NettyKcpClient;
import org.qiunet.flash.handler.netty.client.param.IClientConfig;
import org.qiunet.flash.handler.netty.client.param.KcpClientConfig;
import org.qiunet.flash.handler.netty.client.param.TcpClientConfig;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientConfig;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.client.trigger.IClientTrigger;
import org.qiunet.flash.handler.netty.client.websocket.NettyWebSocketClient;
import org.qiunet.flash.handler.netty.server.config.adapter.message.StatusTipsRsp;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.function.ai.node.IBehaviorAction;
import org.qiunet.function.ai.node.root.BehaviorRootTree;
import org.qiunet.function.ai.observer.IBHTAddNodeObserver;
import org.qiunet.game.test.bt.RobotBehaviorBuilderManager;
import org.qiunet.game.test.response.ResponseMapping;
import org.qiunet.game.test.robot.action.BaseRobotAction;
import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.args.Argument;
import org.qiunet.utils.args.ArgumentKey;
import org.qiunet.utils.args.IArgsContainer;
import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qiunet.
 * 17/12/9
 */
public class Robot extends AbstractMessageActor<Robot> implements IMessageHandler<Robot> , IArgsContainer, IRobot {
	protected static final Logger logger = LoggerType.DUODUO_GAME_TEST.getLogger();
	private static final AtomicInteger counter = new AtomicInteger();
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
	private final Map<String, Connection> clients = Maps.newConcurrentMap();
	/**
	 * 心跳future
	 */
	private DFuture<?> tickFuture;
	/**
	 *
	 */
	private final BehaviorRootTree<Robot> behaviorRootTree;
	/**
	 * robot 心跳时间
	 */
	private final int tickMillis;
	/**
	 * id
	 */
	private long id;
	/**
	 * 账号
	 */
	private final String account;

	public Robot(String account, int tickMillis, boolean printLog) {
		super(account);
		this.behaviorRootTree = RobotBehaviorBuilderManager.instance.newBehaviorRootTree(this, printLog);
		this.behaviorRootTree.attachObserver(IBHTAddNodeObserver.class, o -> {
			if (IBehaviorAction.class.isAssignableFrom(o.getClass())) {
				this.registerAction((BaseRobotAction) o);
			}
		});
		RobotBehaviorBuilderManager.instance.buildRootExecutor(this.behaviorRootTree);

		this.tickFuture = this.scheduleMessage(h -> this.tickRun(), MathUtil.random(200, 500), TimeUnit.MILLISECONDS);
		this.tickMillis = tickMillis;
		counter.incrementAndGet();
		this.account = account;
	}

	public Robot(String account, int tickMillis) {
		this(account, tickMillis, false);
	}

	public Robot(String account, boolean printLog) {
		this(account, 500, printLog);
	}

	public Robot(String account) {
		this(account, 500);
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getAccount() {
		return account;
	}

	public boolean isAuth(){
		return this.id > 0;
	}

	@Override
	public void auth(long id) {
		// do nothing
	}

	@Override
	public String msgExecuteIndex() {
		return this.account;
	}

	@Override
	public void destroy() {
		if (isDestroyed()) {
			return;
		}

		super.destroy();
		this.clients.forEach((key, val) -> {
			val.getSession().close(CloseCause.LOGOUT);
		});
		this.tickFuture.cancel(false);
		counter.decrementAndGet();
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

	/**
	 * 获取已经有的连接
	 * @param name
	 * @return
	 */
	public ClientSession getConnector(String name) {
		if (! clients.containsKey(name)) {
			throw new CustomException("Session {} not connect!", name);
		}
		return clients.get(name).getSession();
	}
	/**
	 * 重连.
	 * @param name
	 * @return
	 */
	public ISession reconnect(String name) {
		if (! clients.containsKey(name)) {
			throw new CustomException("Session {} not connect!", name);
		}
		clients.get(name).closeCurrentSession();
		ISession session1 = clients.get(name).getSession();
		session1.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, this);
		return session1;
	}

	/**
	 * 连接指定服务器
	 * @param config
	 * @param name
	 * @return
	 */
	public ClientSession connect(IClientConfig config, String name) {
		ClientSession session1 = clients.computeIfAbsent(name, serverName -> new Connection(trigger, config)).getSession();
		session1.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, this);
		return session1;
	}

	private static class Connection  {
		/**
		 * 是否连接
		 */
		private final AtomicBoolean connected = new AtomicBoolean();
		private final PersistConnResponseTrigger trigger;
		/**
		 * 配置
		 */
		private final IClientConfig config;
		/**
		 * session
		 */
		private ClientSession session;

		public Connection(PersistConnResponseTrigger trigger, IClientConfig config) {
			this.trigger = trigger;
			this.config = config;
		}

		public void closeCurrentSession() {
			if (this.connected.compareAndSet(true, false)) {
				this.session.close(CloseCause.LOGOUT);
			}
		}

		/**
		 * 获得连接
		 * @return
		 */
		public ClientSession getSession() {
			if (! this.connected.get() && this.connected.compareAndSet(false, true)) {
				this.session = this.connect0();
			}
			return this.session;
		}
		private ClientSession connect0() {
			switch (config.getConnType()) {
				case WS:
					return NettyWebSocketClient.create(((WebSocketClientConfig) config), trigger);
				case KCP:
					return NettyKcpClient.create((KcpClientConfig) config, trigger)
							.connect(config.getAddress().getHostString(), config.getAddress().getPort());
				case TCP:
					return NettyTcpClient.create((TcpClientConfig) config, trigger)
							.connect(config.getAddress().getHostString(), config.getAddress().getPort());
				default:
					throw new CustomException("Type [{}] is not support", config.getConnType());
			}
		}
	}

	private class PersistConnResponseTrigger implements IClientTrigger {

		@Override
		public void response(ClientSession session, IChannelData channelData) {
			Robot.this.addMessage(h -> {
				response0(session, channelData);
			});
		}

		private void response0(ClientSession session, IChannelData response) {
			int protocolId = response.protocolId();
			if (protocolId == IProtocolId.System.ERROR_STATUS_TIPS_RSP) {
				this.handlerStatus((StatusTipsRsp) response);
				return;
			}

			Method method = ResponseMapping.getResponseMethodByID(protocolId);
			if (method == null) {
				if (counter.get() < 5) {
					logger.error("=====Response [{}] not define,skip message!======", response.getClass().getSimpleName());
				}
				return;
			}

			if (logger.isInfoEnabled() && response.debugOut()) {
				logger.info("[{}] <<< {}", getAccount(), response._toString());
			}



			Class<?> declaringClass = method.getDeclaringClass();
			IBehaviorAction action = actionClzMapping.get(declaringClass);

			try {
				method.invoke(action, response);
			} catch (Exception e) {
				throw new CustomException(e, "response exception!");
			}
		}

		private void handlerStatus(StatusTipsRsp response) {
			Set<Method> methods = ResponseMapping.getResponseStatusHandler(response.getStatus());
			if (methods.isEmpty()) {
				// 出现这个. 一般是行为树参数或者逻辑问题,导致服务器校验不通过. 需要调整参数.
				logger.error("[{}] StatusTipsRsp [({}): {}]", Robot.this.getIdentity(), response.getStatus(),response.getDesc());
				return;
			}

			methods.forEach(mtd -> {
				IBehaviorAction<?> action = actionClzMapping.get(mtd.getDeclaringClass());
				try {
					mtd.invoke(action, response);
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			});
		}
	}
	@Override
	public <T> Argument<T> getArgument(ArgumentKey<T> key, boolean computeIfAbsent) {
		return container.getArgument(key, computeIfAbsent);
	}

	public void registerAction(BaseRobotAction action) {
		this.actionClzMapping.put(action.getClass(), action);
	}
}
