package org.qiunet.flash.handler.netty.server.tcp.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerType.DUODUO.getLogger();
	private TcpBootstrapParams params;

	public TcpServerHandler(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(HandlerType.TCP);
		ISession iSession = params.getStartupContext().buildSession(ctx.channel());

		SessionManager.getInstance().addSession(iSession);
		ctx.channel().attr(ServerConstants.PLAYER_ACTOR_KEY).set(params.getStartupContext().buildPlayerActor(iSession));
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

		ISession session = SessionManager.getInstance().getSession(ctx.channel());
		if (session != null && ctx.channel().isActive()) {
			ITcpRequestContext context = handler.getDataType().createTcpRequestContext(content, ctx, handler, session.getPlayerActor());
			session.getPlayerActor().addMessage(context);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Exception : ", cause);
		ctx.writeAndFlush(params.getErrorMessage().exception(cause).encode()).addListener(ChannelFutureListener.CLOSE);
		ctx.close();
	}
}
