package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class WebsocketServerHandler  extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 因为通过http添加的Handler , 所以activate 已经没法调用了. 只能通过handlerShark Complete 事件搞定
		if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
			HttpHeaders headers = ((WebSocketServerProtocolHandler.HandshakeComplete) evt).requestHeaders();
			DSession session = new DSession(ctx.channel());
			ctx.channel().attr(ServerConstants.SESSION_KEY).set(session);

			session.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.WS);
			session.attachObj(ServerConstants.HTTP_WS_HEADER_KEY, headers);
		}
		super.userEventTriggered(ctx, evt);
	}
}
