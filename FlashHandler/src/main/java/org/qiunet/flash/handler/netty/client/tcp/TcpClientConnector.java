package org.qiunet.flash.handler.netty.client.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.future.DChannelFutureWrapper;
import org.qiunet.flash.handler.context.session.future.DMessageContentFuture;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.flash.handler.netty.client.IPersistConnClient;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/***
 * Tcp client 专门连接服务器的对象.
 * bootstrap 复用
 *
 * @author qiunet
 * 2020-11-06 12:25
 */
public class TcpClientConnector implements IPersistConnClient {
	private final ConcurrentLinkedQueue<DMessageContentFuture> queue = new ConcurrentLinkedQueue<>();
	private final AtomicBoolean connecting = new AtomicBoolean();
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

	public DPromise<DSession> getPromise() {
		return promise;
	}

	/**
	 * 连接
	 */
	public void connect(){
		if (! connecting.compareAndSet(false, true)) {
			return;
		}

		this.promise = DPromise.create();
		GenericFutureListener<ChannelFuture> listener = f -> {
			if (! f.isSuccess()) {
				promise.tryFailure(new CustomException("Tcp Connect fail!"));
				return;
			}
			f.channel().attr(NettyTcpClient.SESSION).set(new DSession(f.channel()));
			promise.trySuccess(f.channel().attr(NettyTcpClient.SESSION).get());
			DMessageContentFuture msg;
			while ((msg = queue.poll()) != null) {
				if (msg.isCanceled()) {
					continue;
				}
				ChannelFuture channelFuture = f.channel().writeAndFlush(msg.getMessageContent());

				msg.getListeners().forEach(channelFuture::addListener);
				AtomicReference<DMessageContentFuture.Status> status = msg.getStatus();
				channelFuture.addListener(f0 -> {
					status.compareAndSet(DMessageContentFuture.Status.NONE, DMessageContentFuture.Status.SUCCESS);
				});
			}
			connecting.set(false);
		};

		ChannelFuture connectFuture = bootstrap.connect(host, port);
		connectFuture.addListener(listener);
	}

	private DSession getSession0() {
		if (this.session == null) {
			try {
				this.session = promise.get(5, TimeUnit.SECONDS);
			} catch (Exception e) {
				throw new CustomException(e, e.getMessage());
			}finally {
				this.promise = null;
			}
		}
		return this.session;
	}

	public DSession getSession() {
		return getSession0();
	}

	@Override
	public IDSessionFuture sendMessage(MessageContent content) {
		if (connecting.get()) {
			DMessageContentFuture IDSessionFuture = new DMessageContentFuture(content);
			this.queue.add(IDSessionFuture);
			return IDSessionFuture;
		}
		return new DChannelFutureWrapper(session.channel().writeAndFlush(content));
	}
}
