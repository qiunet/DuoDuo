package org.qiunet.flash.handler.netty.server.tcp.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private TcpBootstrapParams params;

	public TcpServerHandler(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageContent content = ((MessageContent) msg);
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(params.getErrorMessage().getHandlerNotFound()).addListener(ChannelFutureListener.CLOSE);
			ctx.close();
			return;
		}

		ITcpRequestContext context = handler.getDataType().createTcpRequestContext(content, ctx, handler, params);
		if (ctx.channel().isActive()) {
			handler.getHandlerType().processRequest(context);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception : ", cause);
		ctx.writeAndFlush(params.getErrorMessage().exception(cause).encode()).addListener(ChannelFutureListener.CLOSE);
		ctx.close();
	}
}
