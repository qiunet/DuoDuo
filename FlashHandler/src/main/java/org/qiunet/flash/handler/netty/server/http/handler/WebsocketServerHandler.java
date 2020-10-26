package org.qiunet.flash.handler.netty.server.http.handler;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.qiunet.flash.handler.common.annotation.TransmitHandler;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.IMessageToCross;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequestContext;
import org.qiunet.flash.handler.context.session.DSession;
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
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	private HttpBootstrapParams params;


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 因为通过http添加的Handler , 所以activate 已经没法调用了. 只能通过handlerShark Complete 事件搞定
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			HttpHeaders headers = ((WebSocketServerProtocolHandler.HandshakeComplete) evt).requestHeaders();
			ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(HandlerType.WEB_SOCKET);

			DSession iSession = new DSession(ctx.channel());

			ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(params.getStartupContext().buildMessageActor(iSession));
			ctx.channel().attr(ServerConstants.HTTP_WS_HEADER_KEY).set(headers);
			SessionManager.addSession(iSession);
		}
		super.userEventTriggered(ctx, evt);
	}

	public WebsocketServerHandler (HttpBootstrapParams params) {
		this.params = params;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(params.getStartupContext().getHandlerNotFound());
//			ctx.close(); // 应刘文要求. 觉得没必要关闭通道.
			return;
		}

		DSession session = SessionManager.getSession(ctx.channel());
		Preconditions.checkNotNull(session);

		IMessageActor messageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (handler.getClass().isAnnotationPresent(TransmitHandler.class)
		&& messageActor instanceof IMessageToCross
		&& ((IMessageToCross) messageActor).isCrossStatus()) {
			((IMessageToCross) messageActor).writeToCross(content);
			return;
		}

		if (ctx.channel().isActive()) {
			IWebSocketRequestContext context = handler.getDataType().createWebSocketRequestContext(content, ctx.channel(), handler, messageActor);
			messageActor.addMessage(context);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		DSession session = SessionManager.getSession(ctx.channel());
		String errMeg = "Exception session ["+(session != null ? session.toString(): "")+"]";
		logger.error(errMeg, cause);

		if (ctx.channel().isActive() || ctx.channel().isOpen()) {
			ctx.writeAndFlush(params.getStartupContext().exception(cause)).addListener(ChannelFutureListener.CLOSE);
			if (session == null) {
				ctx.close();
			}else {
				session.close(CloseCause.EXCEPTION);
			}
		}
	}
}
