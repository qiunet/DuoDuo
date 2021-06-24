package org.qiunet.flash.handler.netty.client.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.IPersistConnClient;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * Tcp client 专门连接服务器的对象.
 * bootstrap 复用
 *
 * @author qiunet
 * 2020-11-06 12:25
 */
public class TcpClientConnector implements IPersistConnClient {
	private final ConcurrentLinkedQueue<MessageContent> queue = new ConcurrentLinkedQueue<>();
	private final AtomicBoolean connecting = new AtomicBoolean();
	private ChannelFuture connectFuture;
	private DPromise<DSession> promise;
	private final Bootstrap bootstrap;
	private final String host;
	private DSession session;
	private final int port;

	TcpClientConnector(Bootstrap bootstrap, String host, int port) {
		this.bootstrap = bootstrap;
		this.host = host;
		this.port = port;
		this.connect();
	}

	/**
	 * 连接
	 */
	public ChannelFuture connect(){
		if (! connecting.compareAndSet(false, true)) {
			return connectFuture;
		}

		this.promise = DPromise.create();
		GenericFutureListener<ChannelFuture> listener = f -> {
			if (f.isSuccess()) {
				f.channel().attr(NettyTcpClient.SESSION).set(new DSession(f.channel()));
				promise.trySuccess(f.channel().attr(NettyTcpClient.SESSION).get());
			}else {
				promise.tryFailure(new CustomException("Tcp Connect fail!"));
			}
			MessageContent msg;
			while ((msg = queue.poll()) != null) {
				f.channel().writeAndFlush(msg);
			}
			connecting.set(false);
			connectFuture = null;
		};

		this.connectFuture = bootstrap.connect(host, port);
		connectFuture.addListener(listener);
		return connectFuture;
	}

	/**
	 * 得到sessionFuture
	 * @return
	 */
	public ChannelFuture getConnectFuture() {
		return connectFuture;
	}

	private DSession getSession0() {
		if (session == null) {
			try {
				this.session = promise.get(5, TimeUnit.SECONDS);
			} catch (Exception e) {
				throw new CustomException(e, e.getMessage());
			}
		}
		return session;
	}

	public DSession getSession() {
		return getSession0();
	}

	@Override
	public void sendMessage(MessageContent content) {
		if (connecting.get()) {
			this.queue.add(content);
			return;
		}
		session.channel().writeAndFlush(content);
	}
}
