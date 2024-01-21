package org.qiunet.utils.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.listener.hook.ShutdownHookUtil;

import javax.net.ssl.SSLException;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * http 连接管理
 *
 * @author qiunet
 * 2024/1/15 10:26
 ***/
enum HttpChannelManager implements ChannelPoolMap<HttpAddress, ChannelPool> {
	instance;

	private static final AttributeKey<HttpChannel> HTTP_CHANNEL_KEY = AttributeKey.newInstance("http_channel_key");
	/**
	 * 启动用的 bootstrap
	 */
	static final NioEventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("http-client"));

	static final AttributeKey<HttpRequestData> REQ_DATA_KEY = AttributeKey.newInstance("http-request-data");

	/**
	 * 是否需要close channel的key
	 */
	private static final AttributeKey<Boolean> NEED_CLOSE_KEY = AttributeKey.newInstance("need_close_key");
	/**
	 * 池
	 */
	private static final AttributeKey<HttpChannelPool> POOL_KEY = AttributeKey.newInstance("pool_key");
	/**
	 * 真实的池map
	 */
	private static final HttpChannelManager0 realManager = new HttpChannelManager0();
	/**
	 * https 的解析
	 */
	private static final SslContext sslContext = newSslContext();

	static {
		group.scheduleAtFixedRate(() -> {
			long now = System.currentTimeMillis();
			for(Iterator<Map.Entry<HttpAddress, HttpChannelPool>> it = realManager.iterator(); it.hasNext();) {
				Map.Entry<HttpAddress, HttpChannelPool> en = it.next();
				if (now - en.getValue().lastAcquireDt >= TimeUnit.MINUTES.toMillis(5)) {
					en.getValue().close();
					it.remove();
					continue;
				}
				en.getValue().cleanupInactiveChannel();
			}
		}, 30, 60, TimeUnit.SECONDS);
	}
	/**
	 * new 一个新的 SslContext
	 * @return SslContext
	 */
	private static SslContext newSslContext() {
		try {
			return SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE)
				.build();
		} catch (SSLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ChannelPool get(HttpAddress key) {
		return realManager.get(key);
	}

	@Override
	public boolean contains(HttpAddress key) {
		return realManager.contains(key);
	}
	/**
	 * 池
	 */
	private static class HttpChannelManager0 extends AbstractChannelPoolMap<HttpAddress, HttpChannelPool> {

		HttpChannelManager0() {
			ShutdownHookUtil.getInstance().addLast(this::close);
		}

		@Override
		protected HttpChannelPool newPool(HttpAddress address) {
			return new HttpChannelPool(address, 50);
		}
	}
	/***
	 * http 连接池
	 * @author qiunet
	 * 2024/1/16 14:08
	 ***/
	static class HttpChannelPool implements ChannelPool {
		/**
		 * 异步的channel获取队列
		 */
		private final Deque<Promise<Channel>> queue = new LinkedBlockingDeque<>();
		/**
		 * 使用的数量
		 * 即活跃数量
		 */
		private final AtomicInteger activateCount = new AtomicInteger();
		/**
		 * 当前链表数量
		 * 即空闲数量
		 */
		private final AtomicInteger idleCount = new AtomicInteger();
		/**
		 * 链表头 尾
		 */
		private final HttpChannel HEAD = new HttpChannel(null);
		private final HttpChannel TAIL = new HttpChannel(null);

		private final ChannelHandler handler;

		private final Bootstrap bootstrap;

		/**
		 * 最后请求的时间
		 */
		private long lastAcquireDt;
		/**
		 * 最大连接数
		 */
		private final int maxConnections;

		public HttpChannelPool(HttpAddress address, int maxConnections) {
			this.bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) TimeUnit.SECONDS.toMillis(HttpRequest.connectTimeout));
			bootstrap.remoteAddress(address.host(), address.port());
			this.handler = new ChannelHandler(address);
			bootstrap.handler(new ChannelInitializer<>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
					assert ch.eventLoop().inEventLoop();
					handler.channelCreated(ch);
                }
            });
			this.maxConnections = maxConnections;
			HEAD.next = TAIL;
			TAIL.pre = HEAD;
		}

		@Override
		public Future<Channel> acquire() {
			return acquire(group.next().newPromise());
		}

		private synchronized void addToQueue(Promise<Channel> promise, boolean reacquire) {
			// 添加到队列, 等有空闲时候, 再重新请求
			if (reacquire) {
				queue.addFirst(promise);
			}else {
				queue.addLast(promise);
			}
		}

		@Override
		public Future<Channel> acquire(Promise<Channel> promise) {
			return acquire0(promise, false);
		}

		public Future<Channel> acquire0(Promise<Channel> promise, boolean reacquire) {
			int count = activateCount.get();
			if (count >= maxConnections) {
				this.addToQueue(promise, reacquire);
				return promise;
			}

			if (activateCount.compareAndSet(count, count + 1)) {
				Channel channel = this.poll();
				if (channel != null) {
					this.offerChannel(channel, promise);
				}else {
					// new 一个新的
					this.newChannel(promise, 0);
				}
			}else {
				return this.acquire0(promise, reacquire);
			}
			return promise;
		}

		private void newChannel(Promise<Channel> promise, int count) {
			Bootstrap clone = this.bootstrap.clone();
			ChannelFuture future = clone.connect();
			future.addListener(f -> {
				if (! f.isSuccess()) {
					if (count >= 2) {
						// 重试2次
						promise.tryFailure(f.cause());
						this.releaseChannel();
						return;
					}
					this.newChannel(promise, count+1);
					return;
				}
				Channel channel = ((ChannelFuture) f).channel();
				channel.closeFuture().addListener(this::closeChannelListener);
				channel.attr(NEED_CLOSE_KEY).set(false);
				channel.attr(POOL_KEY).set(this);
				this.offerChannel(channel, promise);
			});
		}
		/**
		 * 针对其它情况channel被关闭的监听
		 * 非pool所为
		 * @param future
		 */
		private void closeChannelListener(Future<? super Void> future) {
			HttpChannel httpChannel = ((ChannelFuture) future).channel().attr(HTTP_CHANNEL_KEY).get();
			if (httpChannel != null && httpChannel.linked) {
				synchronized (this) {
					idleCount.decrementAndGet();
					httpChannel.remove();
				}
			}else {
				this.releaseChannel();
			}
		}
		/**
		 * 对外提供channel;
		 * @param channel channel
		 * @param promise promise
		 */
		private void offerChannel(Channel channel, Promise<Channel> promise) {
			lastAcquireDt = System.currentTimeMillis();
			promise.trySuccess(channel);
		}

		private void releaseChannel() {
			activateCount.decrementAndGet();
			this.reacquire();
		}

		private synchronized void reacquire() {
			Promise<Channel> promise = queue.pollFirst();
			if (promise != null) {
				this.acquire0(promise, true);
			}
		}

		@Override
		public Future<Void> release(Channel channel) {
			return release(channel, group.next().newPromise());
		}

		@Override
		public Future<Void> release(Channel channel, Promise<Void> promise) {
			if (! channel.isActive()) {
				promise.trySuccess(null);
				this.reacquire();
				return promise;
			}

			if (channel.eventLoop().inEventLoop()) {
				this.release0(channel, promise);
			}else {
				channel.eventLoop().execute(() -> {
					this.release0(channel, promise);
				});
			}
			return promise;
		}

		private void closeChannel(Channel channel) {
			channel.close();
		}


		public void release0(Channel channel, Promise<Void> promise) {
			if (channel.attr(NEED_CLOSE_KEY).get()) {
				this.closeChannel(channel);
				promise.trySuccess(null);
				return;
			}

			try {
				this.handler.channelReleased(channel);
				this.addToTail(channel);
				promise.trySuccess(null);
			} catch (Exception e) {
				promise.tryFailure(e);
			}finally {
				this.releaseChannel();
			}
		}

		/**
		 * 弹出一个channel
		 * @return httpChannel
		 */
		private synchronized Channel poll(){
			if (idleCount.get() <= 0) {
				return null;
			}
			HttpChannel rh = HEAD.next;
			try {
				while (rh != TAIL) {
					rh.remove();
					idleCount.decrementAndGet();
					if (rh.channel.isActive()) {
						return rh.channel;
					}
					rh = rh.next;
				}
			}finally {
				if (rh != TAIL) {
					rh.cleanup();
				}
			}
			return null;
		}

		/**
		 * 队尾塞一个channel
		 * @param channel
		 */
		private synchronized void addToTail(Channel channel) {
			HttpChannel httpChannel = channel.attr(HTTP_CHANNEL_KEY).get();
			if (httpChannel == null) {
				httpChannel = new HttpChannel(channel);
				channel.attr(HTTP_CHANNEL_KEY).set(httpChannel);
			}
			httpChannel.refreshUseTime();
			httpChannel.pre = TAIL.pre;
			TAIL.pre.next = httpChannel;
			httpChannel.linked = true;
			TAIL.pre = httpChannel;
			httpChannel.next = TAIL;
			idleCount.incrementAndGet();
		}

		/**
		 * 清理不活跃的channel
		 */
		synchronized void cleanupInactiveChannel() {
			long now = System.currentTimeMillis();
			HttpChannel hc = HEAD.next;
			while (hc != TAIL) {
				HttpChannel hcNow = hc;
				hc = hc.next;
				if (now - hcNow.useTime > TimeUnit.MINUTES.toMillis(1)) {
					hcNow.channel.close();
				}
			}
		}

		@Override
		public synchronized void close() {
			HttpChannel hc = HEAD.next;
			while (hc != TAIL) {
				HttpChannel hcNow = hc;
				hc = hc.next;
				hcNow.channel.close();
			}
		}
	}

	/**
	 * 池的channel处理
	 * @param address
	 */
	private record ChannelHandler(HttpAddress address) implements ChannelPoolHandler {

		@Override
		public void channelReleased(Channel ch) throws Exception {
		}

		@Override
		public void channelAcquired(Channel ch) throws Exception {
		}

		@Override
		public void channelCreated(Channel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			if (address.ssl()) {
				pipeline.addFirst("ssl", new SslHandler(sslContext.newEngine(ch.alloc())));
			}
			pipeline.addLast("codec", new HttpClientCodec());
			pipeline.addLast("aggregator", new HttpObjectAggregator(HttpRequest.maxReceivedContentLength));
			pipeline.addLast("handler", new NettyHttpClientHandler());
		}
	}

	/**
	 * 客户端的处理
	 */
	private static class NettyHttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
			HttpRequestData requestData = ctx.channel().attr(REQ_DATA_KEY).get();
			if (! requestData.getTimeout().cancel()) {
				// 已经超时处理了!
				return;
			}

			try {
				DecoderResult decoderResult = response.decoderResult();
				if (decoderResult.isFailure()) {
					requestData.fail(decoderResult.cause());
					return;
				}

				requestData.success(response);
			}finally {
				if (! HttpUtil.isKeepAlive(requestData.getRequest())) {
					ctx.channel().attr(NEED_CLOSE_KEY).set(true);
				}
				ctx.channel().attr(POOL_KEY).get().release(ctx.channel());
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			HttpRequestData requestData = ctx.channel().attr(REQ_DATA_KEY).get();
			ctx.channel().attr(NEED_CLOSE_KEY).set(true);
			ctx.channel().attr(POOL_KEY).get().release(ctx.channel());
			requestData.fail(cause);
		}
	}
	/**
	 * Channel 包装类
	 */
	private static class HttpChannel {
		private HttpChannel pre,next;
		/**
		 * channel
		 */
		Channel channel;
		/**
		 * 最后使用时间,用来移除keep alive中没有活跃的channel
		 */
		long useTime;
		/**
		 * 是否队列中
		 */
		boolean linked;

		HttpChannel(Channel channel) {
			this.refreshUseTime();
			this.channel = channel;
		}

		/**
		 * 刷新最后的使用时间
		 */
		void refreshUseTime() {
			this.useTime = System.currentTimeMillis();
		}
		/**
		 * 清理
		 */
		void cleanup() {
			pre = next = null;
		}
		/**
		 * 从链表移除
		 */
		void remove() {
			linked = false;
			pre.next = next;
			next.pre = pre;
		}
	}
}

