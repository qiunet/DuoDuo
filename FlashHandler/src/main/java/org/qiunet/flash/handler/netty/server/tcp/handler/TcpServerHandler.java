package org.qiunet.flash.handler.netty.server.tcp.handler;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private TcpBootstrapParams params;

	public TcpServerHandler(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(HandlerType.TCP);
		DSession session = new DSession(ctx.channel());

		SessionManager.getInstance().addSession(session);
		ctx.channel().attr(ServerConstants.PLAYER_ACTOR_KEY).set(params.getStartupContext().buildPlayerActor(session));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageContent content = ((MessageContent) msg);
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(params.getStartupContext().getHandlerNotFound());
			ctx.close();
			return;
		}

		DSession session = SessionManager.getInstance().getSession(ctx.channel());
		Preconditions.checkNotNull(session);

		IPlayerActor playerActor = session.getAttachObj(ServerConstants.PLAYER_ACTOR_KEY);
		if (handler.needAuth() && ! playerActor.isAuth()) {
			session.close(CloseCause.ERR_REQUEST);
			return;
		}

		if (ctx.channel().isActive()) {
			ITcpRequestContext context = handler.getDataType().createTcpRequestContext(content, ctx, handler, playerActor);
			playerActor.addMessage(context);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		DSession session = SessionManager.getInstance().getSession(ctx.channel());
		String errMeg = "Exception session ["+(session != null ? session.toString(): "null")+"]";
		logger.error(errMeg, cause);

		if (ctx.channel().isOpen() || ctx.channel().isActive()) {
			ctx.writeAndFlush(params.getStartupContext().exception(cause).encode()).addListener(ChannelFutureListener.CLOSE);
			if (session != null) {
				session.close(CloseCause.EXCEPTION);
			}else {
				ctx.close();
			}
		}
	}
}
