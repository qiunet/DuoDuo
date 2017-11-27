package org.qiunet.flash.handler.netty.server.tcp.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import org.qiunet.flash.handler.acceptor.Acceptor;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.session.IPlayerSession;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	private static final SessionManager<String, ISession<String>> sessionManager = SessionManager.getInstance();

	private static final QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
	private Acceptor acceptor = Acceptor.getInstance();
	private TcpBootstrapParams params;

	public TcpServerHandler(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		params.getSessionEvent().sessionRegistered(ctx);
		sessionManager.addSession(params.getSessionBuilder().createSession(ctx));
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		params.getSessionEvent().sessionUnregistered(ctx);
		sessionManager.removeSession(ctx.channel().id().asLongText());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageContent content = ((MessageContent) msg);
		IHandler handler = params.getAdapter().getHandler(content);
		if (handler == null) {
			ctx.channel().writeAndFlush(params.getErrorMessage().getHandlerNotFound()).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		ITcpRequestContext context = params.getAdapter().createTcpRequestContext(content, ctx, handler, params);
		acceptor.process(context);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception : ", cause);
		ctx.close();
	}
}
