package org.qiunet.flash.handler.context.session;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.config.DSessionConfig;
import org.qiunet.flash.handler.context.session.config.DSessionConnectParam;
import org.qiunet.flash.handler.context.session.future.DChannelFutureWrapper;
import org.qiunet.flash.handler.context.session.future.DMessageContentFuture;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Session 的 父类
 * Created by qiunet.
 * 17/11/26
 */
public class DSession implements IChannelMessageSender {
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 如果是使用DSession 连接, 连接成功前. 发送的消息存储这里
	 */
	private final ConcurrentLinkedQueue<DMessageContentFuture> queue = new ConcurrentLinkedQueue<>();
	/**
	 * 锁. 防止连接时候. 发送信息错误放置.
	 */
	private final ReentrantLock sessionLock = new ReentrantLock();
	/**
	 * 连接中标志
	 */
	private final AtomicBoolean connecting = new AtomicBoolean();
	/**
	 * 配置
	 */
	private DSessionConfig sessionConfig = DSessionConfig.DEFAULT_CONFIG;
	/**
	 * 写次数计数
	 */
	private final AtomicInteger counter = new AtomicInteger();
	/**
	 * 判断是否已经在计时flush
	 */
	private final AtomicBoolean flushScheduling = new AtomicBoolean();
	/**
	 * 如果是作为客户端的DSession, 这里是连接参数
	 */
	private DSessionConnectParam connectParam;
	/**
	 * netty Channel
	 */
	private Channel channel;

	protected DSession(){}
	/**
	 * 作为客户端. 也可以先使用连接参数. 构造一个DSession. 先发送消息.
	 * 消息在连接前缓存
	 * @param connectParam
	 */
	public DSession(DSessionConnectParam connectParam) {
		this.connectParam = connectParam;
		this.connect();
	}

	public DSession(Channel channel) {
		this.setChannel(channel);
	}

	/**
	 * 连接
	 */
	private void connect() {
		Preconditions.checkNotNull(connectParam);
		if (! connecting.compareAndSet(false, true)) {
			return;
		}
		GenericFutureListener<ChannelFuture> listener = f -> {
			if (! f.isSuccess()) {
				throw new CustomException("Tcp Connect fail!");
			}
			f.channel().attr(ServerConstants.SESSION_KEY).set(this);
			this.setChannel(f.channel());
			try {
				sessionLock.lock();
				DMessageContentFuture msg = queue.poll();
				if (msg != null) {
					// 第一个协议一般是鉴权协议. 先发送. 等发送成功再发送后面的协议.
					IDSessionFuture future = this.doSendMessage(msg.getMessage(), true);
					future.addListener(f0 -> {
						if (f0.isSuccess()) {
							msg.complete(f0);

							DMessageContentFuture msg0;
							while ((msg0 = queue.poll()) != null) {
								if (msg0.isCanceled()) {
									continue;
								}

								IDSessionFuture future1 = this.doSendMessage(msg0.getMessage(), false);
								DMessageContentFuture finalMsg = msg0;
								future1.addListener(f1 -> {
									if (f1.isSuccess()) {
										finalMsg.complete(f1);
									}
								});
							}
						}
						this.flush0();
					});
				}
				connecting.set(false);
			}finally {
				sessionLock.unlock();
			}
		};

		ChannelFuture connectFuture = connectParam.connect();
		connectFuture.addListener(listener);
	}

	private void setChannel(Channel channel) {
		if (channel != null) {
			// 测试可能为null
			channel.closeFuture().addListener(f -> this.close(CloseCause.CHANNEL_CLOSE));
		}
		this.channel = channel;
	}
	/**
	 * 设置 session 的参数
	 */
	public DSession sessionConfig(DSessionConfig config) {
		Preconditions.checkState(config.isDefault_flush() || (config.getFlush_delay_ms() >= 5 && config.getFlush_delay_ms() < 10000));
		this.sessionConfig = config;
		return this;
	}

	/**
	 * session是否是活跃的.
	 * @return
	 */
	public boolean isActive() {
		return channel != null && channel.isActive();
	}

	/**
	 * 得到channel
	 * 如果是client模式. 会阻塞知道拿到channel
	 *
	 * @return
	 */
	public Channel channel() {
		return channel;
	}

	/**
	 * 获得ip
	 * @return
	 */
	public String getIp() {
		return ChannelUtil.getIp(channel);
	}

	private ScheduledFuture<?> flushSchedule;
	/**
	 * flush
	 */
	private void flush0(){
		flushScheduling.set(false);
		this.flushSchedule = null;
		counter.set(0);
		channel.flush();
	}

	/**
	 * 获得channel里面的对象.
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> T getAttachObj(AttributeKey<T> key) {
		return channel.attr(key).get();
	}

	/**
	 * 往channel上面挂载数据.
	 * @param key
	 * @param obj
	 * @param <T>
	 */
	public <T> void attachObj(AttributeKey<T> key, T obj) {
		channel.attr(key).set(obj);
	}

	private final AtomicBoolean closed = new AtomicBoolean();
	public void close(CloseCause cause) {
		if (! closed.compareAndSet(false, true)) {
			// 避免多次调用close. 多次调用监听.
			return;
		}
		logger.info("Session [{}] closed by cause [{}]", this, cause.getDesc());
		closeListeners.forEach(l -> l.close(cause));
		if (channel != null && (channel.isActive() || channel.isOpen())) {
			channel.close();
		}
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(",", "[", "]");
		if (channel != null) {
			IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			if (messageActor != null) {
				sj.add(messageActor.getIdentity());
			}
			sj.add("ID = " + channel.id().asShortText());
			sj.add("Ip = " + getIp());
		}
		return sj.toString();
	}

	/**
	 * 添加一个close监听.
	 * @param listener
	 */
	public void addCloseListener(SessionCloseListener listener) {
		this.closeListeners.add(listener);
	}

	private final List<SessionCloseListener> closeListeners = Lists.newCopyOnWriteArrayList();

	@Override
	public DSession getSender() {
		return this;
	}

	@Override
	public IDSessionFuture sendMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, sessionConfig.isDefault_flush());
	}

	@Override
	public IDSessionFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		if (connecting.get()) {
			try {
				sessionLock.lock();
				if (connecting.get()) {
					DMessageContentFuture contentFuture = new DMessageContentFuture(message);
					this.queue.add(contentFuture);
					return contentFuture;
				}
			}finally {
				sessionLock.unlock();
			}
		}
		return this.doSendMessage(message, flush);
	}

	public IDSessionFuture doSendMessage(IChannelMessage<?> message, boolean flush) {
		if ( logger.isInfoEnabled()
				&& message.needLogger()) {
			IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			if (messageActor != null) {
				logger.info("[{}] >>> {}", messageActor.getIdentity(), message.toStr());
			}
		}

		if (flush) {
			return new DChannelFutureWrapper(channel.writeAndFlush(message.encode()));
		}

		IDSessionFuture future = new DChannelFutureWrapper(channel.write(message.encode()));
		if (counter.incrementAndGet() >= 10) {
			// 次数够也flush
			if (this.flushSchedule != null && ! this.flushSchedule.isDone()) {
				this.flushSchedule.cancel(false);
			}
			this.flush0();
			return future;
		}

		if (flushScheduling.compareAndSet(false, true)) {
			this.flushSchedule = channel.eventLoop().schedule(this::flush0, sessionConfig.getFlush_delay_ms(), TimeUnit.MILLISECONDS);
		}
		return future;
	}

	@FunctionalInterface
	public interface SessionCloseListener {
		void close(CloseCause cause);
	}
}
