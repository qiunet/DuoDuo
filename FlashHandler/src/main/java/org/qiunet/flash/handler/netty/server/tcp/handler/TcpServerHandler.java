package org.qiunet.flash.handler.netty.server.tcp.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.qiunet.flash.handler.acceptor.Acceptor;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.FLASH_HANDLER);
	private Acceptor acceptor = Acceptor.getInstance();
	private TcpBootstrapParams params;

	public TcpServerHandler(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		params.getSessionEvent().sessionUnregistered(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		params.getSessionEvent().sessionRegistered(ctx);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageContent content = ((MessageContent) msg);
		IHandler handler = params.getAdapter().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(params.getErrorMessage().getHandlerNotFound()).addListener(ChannelFutureListener.CLOSE);
			ctx.close();
			return;
		}

		ITcpRequestContext context = params.getAdapter().createTcpRequestContext(content, ctx, handler, params);
		params.getSessionEvent().sessionReceived(ctx, HandlerType.TCP, context);
		acceptor.process(context);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception : ", cause);
		ctx.writeAndFlush(params.getErrorMessage().exception(cause).encode()).addListener(ChannelFutureListener.CLOSE);
		ctx.close();
	}
}
