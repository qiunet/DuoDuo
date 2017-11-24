package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.qiunet.flash.handler.acceptor.Acceptor;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.context.request.http.IHttpRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.bytebuf.PooledBytebufFactory;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

import java.net.URI;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * handler 是每次处理new 一个新的. 如果keep alive 则使用同一个实例.
 *
 * Created by qiunet.
 * 17/11/11
 */
public class HttpServerHandler  extends SimpleChannelInboundHandler<Object> {
	private static final Acceptor acceptor = Acceptor.getInstance();

	private static final QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

	private HttpBootstrapParams params;

	private HttpRequest request;

	private ByteBuf byteBuf;


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
			this.request = (HttpRequest) msg;
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
				try {
					URI uri = URI.create(request.uri());
					if (params.getGameURIPath().equals(uri.getRawPath())) {
						handlerGameUriPathRequest(ctx);
					}else {
						handlerOtherUriPathRequest(ctx, uri.getRawPath());
					}
				}catch (Exception e) {
					sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);

					logger.error("HttpServerHandler Parse request error: ", e);
				}finally {
					this.request = null;
				}
			}
		}
	}
	/***
	 * 处理其它请求
	 * @return
	 */
	private void handlerGameUriPathRequest(ChannelHandlerContext ctx){
		ProtocolHeader header = new ProtocolHeader(byteBuf);
		byte [] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		MessageContent content = new MessageContent(header.getProtocolId(), bytes);
		IHandler handler = params.getAdapter().getHandler(content);
		if (handler == null) {
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}

		IHttpRequestContext context = params.getAdapter().createHttpRequestContext(content, ctx, handler, params, request);
		acceptor.process(context);
	}
	/***
	 * 处理其它请求
	 * @return
	 */
	private void handlerOtherUriPathRequest(ChannelHandlerContext ctx, String uriPath){
		byte [] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		MessageContent content = new MessageContent(uriPath, bytes);
		IHandler handler = params.getAdapter().getHandler(content);
		if (handler == null) {
			sendHttpResonseStatusAndClose(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}

		IHttpRequestContext context = params.getAdapter().createHttpRequestContext(content, ctx, handler, params, request);
		acceptor.process(context);
	}


	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.write(response);
	}

	/***
	 * 发送响应.
	 * @param ctx
	 * @param status 对应的响应码
	 */
	private static void sendHttpResonseStatusAndClose(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
}
