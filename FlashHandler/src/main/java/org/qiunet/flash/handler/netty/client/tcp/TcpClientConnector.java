package org.qiunet.flash.handler.netty.client.tcp;

import com.google.common.base.Preconditions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.utils.async.future.DCompletePromise;
import org.qiunet.utils.async.future.DPromise;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.TimeUnit;

/***
 * Tcp client 专门连接服务器的对象.
 * bootstrap 复用
 *
 * @author qiunet
 * 2020-11-06 12:25
 */
public class TcpClientConnector implements ILongConnClient {
	private DSession session;

	TcpClientConnector(Bootstrap bootstrap, String host, int port) {
		DPromise<DSession> promise = new DCompletePromise<>();
		GenericFutureListener<ChannelFuture> listener = f -> {
			if (f.isSuccess()) {
				f.channel().attr(NettyTcpClient.SESSION).set(new DSession(f.channel()));
				promise.trySuccess(f.channel().attr(NettyTcpClient.SESSION).get());
			}else {
				promise.tryFailure(new CustomException("Tcp Connect fail!"));
			}
		};

		ChannelFuture channelFuture = bootstrap.connect(host, port);
		channelFuture.addListener(listener);
		try {
			this.session = promise.get(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new CustomException(e, e.getMessage());
		}
	}

	public DSession getSession() {
		return session;
	}

	@Override
	public void sendMessage(MessageContent content) {
		Preconditions.checkState(session != null && session.isActive());
		session.channel().writeAndFlush(content);
	}
}
