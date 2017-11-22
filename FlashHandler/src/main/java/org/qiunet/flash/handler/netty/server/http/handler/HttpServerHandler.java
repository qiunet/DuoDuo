package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.qiunet.flash.handler.acceptor.Acceptor;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;
import org.qiunet.flash.handler.netty.param.HttpBootstrapParams;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by qiunet.
 * 17/11/11
 */
public class HttpServerHandler  extends SimpleChannelInboundHandler<Object> {

	private HttpBootstrapParams params;

	private HttpRequest request;

	private ByteBuf byteBuf;

	private Acceptor acceptor = Acceptor.getInstance();

	public HttpServerHandler (HttpBootstrapParams params) {
		this.params = params;
		this.byteBuf = PooledBytebufFactory.getInstance().alloc();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = this.request = (HttpRequest) msg;
			this.byteBuf.clear();

			if (HttpUtil.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}
		}
		if (msg instanceof HttpContent) {
			if (((HttpContent) msg).content().isReadable()) {
				byteBuf.writeBytes(((HttpContent) msg).content());
			}
			if (msg instanceof LastHttpContent) {
				ProtocolHeader header = new ProtocolHeader(byteBuf);
				byte [] bytes = new byte[byteBuf.readableBytes()];
				byteBuf.readBytes(bytes);
				MessageContent content = new MessageContent(header.getProtocolId(), header.getSequence(), bytes);

				IHttpRequestContext context = params.getAdapter().createHttpRequestContext(content, ctx, request);
				acceptor.process(context);

				this.request = null;
			}
		}
	}

	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
		ctx.write(response);
	}
}
