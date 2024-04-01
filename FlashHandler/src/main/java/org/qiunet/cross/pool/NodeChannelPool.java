package org.qiunet.cross.pool;

import com.google.common.collect.Sets;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import org.qiunet.utils.logger.LoggerType;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

/***
 * Channel Pool
 *
 * @author qiunet
 * 2023/3/31 13:41
 */
public class NodeChannelPool implements ChannelPool {
	public static final AttributeKey<AtomicInteger> COUNTER_KEY = AttributeKey.newInstance("org.qiunet.cross.pool.NodeChannelPool.counter");
	private static final AttributeKey<NodeChannelPool> POOL_KEY = AttributeKey.newInstance("io.netty.channel.pool.NodeChannelPool");
	private final Set<Channel> channelSet = Sets.newConcurrentHashSet();
	private final ChannelHealthChecker healthCheck;
	private final ChannelPoolHandler handler;
	private final boolean releaseHealthCheck;
	private final int maxConnections;
	private final Bootstrap bootstrap;

	public NodeChannelPool(Bootstrap bootstrap, NodeChannelTrigger channelTrigger, boolean releaseHealthCheck, int maxMsgLength, int maxConnections) {
		this.handler = new NodeChannelPoolHandler(channelTrigger, maxMsgLength);
		this.healthCheck = ChannelHealthChecker.ACTIVE;
		this.releaseHealthCheck = releaseHealthCheck;
		this.maxConnections = maxConnections;
		this.bootstrap = bootstrap;
		this.bootstrap.handler(new ChannelInitializer<>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				handler.channelCreated(ch);
			}
		});
	}

	@Override
	public Future<Channel> acquire() {
		return acquire(bootstrap.config().group().next().<Channel>newPromise());
	}

	@Override
	public Future<Channel> acquire(Promise<Channel> promise) {
		return acquireHealthyFromPoolOrNew(checkNotNull(promise, "promise"));
	}

	/**
	 * 从当前channel Map 挑选一个Channel给出.
	 * 如果当前channel有5个用户在用了, 就new一个新的
	 * 达到maxConnections后, 优先分配少人用的.
	 * @return channel
	 */
	private Channel acquireChannel() {
		int size = channelSet.size();
		if (size < maxConnections) {
			return null;
		}

		Channel channel = null;
		int min = 0;
		for(Channel channel0: channelSet) {
			AtomicInteger atomicInteger = channel0.attr(COUNTER_KEY).get();
			if (channel == null || atomicInteger.get() < min) {
				min = atomicInteger.get();
				channel = channel0;
			}
		}
		return channel;
	}

	private Future<Channel> acquireHealthyFromPoolOrNew(final Promise<Channel> promise) {
		try {
			final Channel ch = acquireChannel();
			if (ch == null) {
				// No Channel left in the pool bootstrap a new Channel
				Bootstrap bs = bootstrap.clone();
				bs.attr(POOL_KEY, this);
				ChannelFuture f = bs.connect();
				if (f.isDone()) {
					notifyConnect(f, promise);
				} else {
					f.addListener((ChannelFutureListener)cf -> notifyConnect(cf, promise));
				}
			} else {
				EventLoop loop = ch.eventLoop();
				if (loop.inEventLoop()) {
					doHealthCheck(ch, promise);
				} else {
					loop.execute(() -> doHealthCheck(ch, promise));
				}
			}
		} catch (Throwable cause) {
			promise.tryFailure(cause);
		}
		return promise;
	}

	private void notifyConnect(ChannelFuture future, Promise<Channel> promise) {
		Channel channel = null;
		try {
			if (future.isSuccess()) {
				channel = future.channel();
				channelSet.add(channel);
				channel.attr(COUNTER_KEY).set(new AtomicInteger());
				channel.closeFuture().addListener(f -> {
					LoggerType.DUODUO_FLASH_HANDLER.info("Channel pool channel {} release!", ((DefaultChannelPromise) f).channel().id().asShortText());
					channelSet.remove(((DefaultChannelPromise) f).channel());
				});
				handler.channelAcquired(channel);
				if (!promise.trySuccess(channel)) {
					// Promise was completed in the meantime (like cancelled), just release the channel again
					release(channel);
				}
			} else {
				promise.tryFailure(future.cause());
			}
		} catch (Throwable cause) {
			closeAndFail(channel, cause, promise);
		}
	}

	private void doHealthCheck(final Channel channel, final Promise<Channel> promise) {
		try {
			assert channel.eventLoop().inEventLoop();
			Future<Boolean> f = healthCheck.isHealthy(channel);
			if (f.isDone()) {
				notifyHealthCheck(f, channel, promise);
			} else {
				f.addListener((FutureListener<Boolean>)cf -> notifyHealthCheck(cf, channel, promise));
			}
		} catch (Throwable cause) {
			closeAndFail(channel, cause, promise);
		}
	}

	private void notifyHealthCheck(Future<Boolean> future, Channel channel, Promise<Channel> promise) {
		try {
			assert channel.eventLoop().inEventLoop();
			if (future.isSuccess() && future.getNow()) {
				channel.attr(POOL_KEY).set(this);
				handler.channelAcquired(channel);
				promise.setSuccess(channel);
			} else {
				closeChannel(channel);
				acquireHealthyFromPoolOrNew(promise);
			}
		} catch (Throwable cause) {
			closeAndFail(channel, cause, promise);
		}
	}

	@Override
	public Future<Void> release(Channel channel) {
		return release(channel, channel.eventLoop().<Void>newPromise());
	}

	@Override
	public Future<Void> release(Channel channel, Promise<Void> promise) {
		try {
			checkNotNull(channel, "channel");
			checkNotNull(promise, "promise");
			EventLoop loop = channel.eventLoop();
			if (loop.inEventLoop()) {
				doReleaseChannel(channel, promise);
			} else {
				loop.execute(() -> doReleaseChannel(channel, promise));
			}
		} catch (Throwable cause) {
			closeAndFail(channel, cause, promise);
		}
		return promise;
	}


	private void closeChannel(Channel channel) throws Exception {
		channel.attr(POOL_KEY).getAndSet(null);
		channel.close();
	}


	private void closeAndFail(Channel channel, Throwable cause, Promise<?> promise) {
		if (channel != null) {
			try {
				closeChannel(channel);
			} catch (Throwable t) {
				promise.tryFailure(t);
			}
		}
		promise.tryFailure(cause);
	}


	private void doReleaseChannel(Channel channel, Promise<Void> promise) {
		try {
			assert channel.eventLoop().inEventLoop();
			if (channel.attr(POOL_KEY).get() != this) {
				closeAndFail(channel, new IllegalArgumentException("Channel " + channel + " was not acquired from this ChannelPool"), promise);
			} else {
				if (releaseHealthCheck) {
					doHealthCheckOnRelease(channel, promise);
				} else {
					releaseAndOffer(channel, promise);
				}
			}
		} catch (Throwable cause) {
			closeAndFail(channel, cause, promise);
		}
	}

	private void releaseAndOffer(Channel channel, Promise<Void> promise) throws Exception {
		handler.channelReleased(channel);
		promise.setSuccess(null);
	}

	private void doHealthCheckOnRelease(final Channel channel, final Promise<Void> promise) throws Exception {
		final Future<Boolean> f = healthCheck.isHealthy(channel);
		if (f.isDone()) {
			releaseAndOfferIfHealthy(channel, promise, f);
		} else {
			f.addListener((FutureListener<Boolean>)cf -> releaseAndOfferIfHealthy(channel, promise, cf));
		}
	}

	private void releaseAndOfferIfHealthy(Channel channel, Promise<Void> promise, Future<Boolean> future) {
		try {
			if (future.getNow()) { //channel turns out to be healthy, offering and releasing it.
				releaseAndOffer(channel, promise);
			} else { //channel not healthy, just releasing it.
				handler.channelReleased(channel);
				promise.setSuccess(null);
			}
		} catch (Throwable cause) {
			closeAndFail(channel, cause, promise);
		}
	}

	@Override
	public void close() {
		for (Channel channel : channelSet) {
			channel.close().awaitUninterruptibly();
		}
	}
}
