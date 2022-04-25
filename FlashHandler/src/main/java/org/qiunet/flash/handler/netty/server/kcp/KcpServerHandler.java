package org.qiunet.flash.handler.netty.server.kcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.KcpSession;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.mapping.KcpPlayerTokenMapping;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpBindAuthReq;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpBindAuthRsp;
import org.qiunet.flash.handler.netty.server.param.KcpBootstrapParams;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/***
 *
 * @author qiunet
 * 2022/4/25 18:19
 */
public class KcpServerHandler extends SimpleChannelInboundHandler<MessageContent> {

	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final KcpBootstrapParams params;

	public KcpServerHandler(KcpBootstrapParams params) {
		this.params = params;
	}



	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(ServerConnType.KCP);
		DSession session = new DSession(ctx.channel());

		ChannelUtil.bindSession(session);
		ctx.channel().attr(ServerConstants.HANDLER_PARAM_KEY).set(params);
		if (! params.isDependOnTcpWs()) {
			// 从tcp那取到PlayerActor
			ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(params.getStartupContext().buildMessageActor(session));
		}
		ctx.fireChannelActive();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		try {
			channelRead1(ctx, content);
		}finally {
			ctx.fireChannelRead(content);
		}
	}

	public void channelRead1(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		if (ChannelUtil.handlerPing(ctx.channel(), content)) {
			return;
		}

		// 鉴权协议. 用来绑定PlayerActor
		if (content.getProtocolId() == IProtocolId.System.KCP_BIND_AUTH_REQ) {
			KcpBindAuthReq req = ProtobufDataManager.decode(KcpBindAuthReq.class, content.byteBuffer());
			content.release();

			PlayerActor playerActor = KcpPlayerTokenMapping.getPlayer(req.getToken());
			ctx.writeAndFlush(KcpBindAuthRsp.valueOf(playerActor != null));
			if (playerActor != null) {
				// 从mapping那取到PlayerActor
				playerActor.getSession().bindKcpSession(((KcpSession) ChannelUtil.getSession(ctx.channel())));
				ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(playerActor);
			}
		}


		ChannelUtil.channelRead(ctx.channel(), params, content);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ChannelUtil.cause(params.getStartupContext(), ctx.channel(), cause);
	}
}
