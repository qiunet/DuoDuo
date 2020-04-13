package org.qiunet.flash.handler.netty.server.http.handler;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class WebsocketServerHandler  extends SimpleChannelInboundHandler<MessageContent> {
	private static final Logger logger = LoggerType.DUODUO.getLogger();

	private HttpBootstrapParams params;
	private HttpHeaders headers;


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 因为通过http添加的Handler , 所以activate 已经没法调用了. 只能通过handlerShark Complete 事件搞定
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(HandlerType.WEB_SOCKET);

			ISession iSession = params.getStartupContext().buildSession(ctx.channel());

			ctx.channel().attr(ServerConstants.PLAYER_ACTOR_KEY).set(params.getStartupContext().buildPlayerActor(iSession));
			SessionManager.getInstance().addSession(iSession);
		}
		super.userEventTriggered(ctx, evt);
	}

	public WebsocketServerHandler (HttpHeaders headers, HttpBootstrapParams params) {
		this.params = params;
		this.headers = headers;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(params.getStartupContext().getHandlerNotFound().encode());
//			ctx.close(); // 应刘文要求. 觉得没必要关闭通道.
			return;
		}

		ISession session = SessionManager.getInstance().getSession(ctx.channel());
		Preconditions.checkNotNull(session);
		IPlayerActor playerActor = session.getPlayerActor();
		if (handler.needAuth() && ! playerActor.isAuth()) {
			session.close(CloseCause.ERR_REQUEST);
			return;
		}

		if (ctx.channel().isActive()) {
			IWebSocketRequestContext context = handler.getDataType().createWebSocketRequestContext(content, ctx, handler, playerActor, headers);
			playerActor.addMessage(context);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ISession session = SessionManager.getInstance().getSession(ctx.channel());
		String errMeg = "Exception openId ["+(session != null ? session.getOpenId(): '-')+"]"+
						"Ip["+(session != null ? session.getIp() : "" )+"]" ;
		logger.error(errMeg, cause);

		if (ctx.channel().isActive() || ctx.channel().isOpen()) {
			ctx.writeAndFlush(params.getStartupContext().exception(cause).encode()).addListener(ChannelFutureListener.CLOSE);
			if (session == null) {
				ctx.close();
			}else {
				session.close(CloseCause.EXCEPTION);
			}
		}
	}
}
