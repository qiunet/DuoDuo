package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.qiunet.flash.handler.acceptor.Acceptor;
import org.qiunet.flash.handler.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/11
 */
public class HttpServerHandler  extends SimpleChannelInboundHandler<Object> {
	private HttpBootstrapParams params;

	private Acceptor acceptor = Acceptor.getInstance();

	public HttpServerHandler (HttpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

	}
}
