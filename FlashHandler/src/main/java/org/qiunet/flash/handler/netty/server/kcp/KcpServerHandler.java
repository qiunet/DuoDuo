package org.qiunet.flash.handler.netty.server.kcp;

import io.jpower.kcp.netty.UkcpServerChildChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.KcpSession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.kcp.observer.IKcpUsabilityChange;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.mapping.KcpPlayerTokenMapping;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpBindAuthReq;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpBindAuthRsp;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpConnectRsp;
import org.qiunet.flash.handler.netty.server.param.KcpBootstrapParams;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.ToString;
import org.slf4j.Logger;

import java.util.Objects;

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
		ISession session = new KcpSession(ctx.channel());

		ChannelUtil.bindSession(session);
		logger.debug("Kcp session {} active!", session);
		ctx.channel().attr(ServerConstants.HANDLER_PARAM_KEY).set(params);
		if (! params.isDependOnTcpWs()) {
			// 从tcp那取到PlayerActor
			ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(params.getStartupContext().buildMessageActor(session));
			PlayerActor playerActor = (PlayerActor) ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).get();
			session.addCloseListener("IKcpUsabilityLose", (session0, cause) -> {
				playerActor.syncFireObserver(IKcpUsabilityChange.class, o -> o.ability(false));
			});
		}
		ctx.fireChannelActive();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		if (ChannelUtil.handlerPing(ctx.channel(), content)) {
			return;
		}

		//
		if (content.getProtocolId() == IProtocolId.System.KCP_CONNECT_REQ) {
			if (logger.isInfoEnabled()) {
				logger.debug("{} <<< Kcp connect req", "KCP");
			}
			ChannelUtil.getSession(ctx.channel()).sendMessage(KcpConnectRsp.valueOf(((UkcpServerChildChannel) ctx.channel()).conv()));
			return;
		}

		// 鉴权协议. 用来绑定PlayerActor
		if (content.getProtocolId() == IProtocolId.System.KCP_BIND_AUTH_REQ) {
			KcpBindAuthReq req = ProtobufDataManager.decode(KcpBindAuthReq.class, content.byteBuffer());
			if (logger.isInfoEnabled()) {
				logger.info("{} <<< {}", "KCP", ToString.toString(req));
			}

			KcpPlayerTokenMapping kcpParamInfo = KcpPlayerTokenMapping.getPlayer(req.getPlayerId());
			if (kcpParamInfo == null
				|| ! Objects.equals(req.getToken(), kcpParamInfo.getToken())
			 	//|| ((UkcpChannel) ctx.channel()).conv() != kcpParamInfo.getConvId()
			) {
				if (kcpParamInfo == null) {
					logger.error("ID: {} kcpParamInfo null, is online: {}", req.getPlayerId(), UserOnlineManager.getPlayerActor(req.getPlayerId()) == null);
				}else if (! Objects.equals(req.getToken(), kcpParamInfo.getToken())) {
					logger.error("ID: {} token error, {} and {}", req.getPlayerId(), req.getToken(), kcpParamInfo.getToken());
				}
				ChannelUtil.getSession(ctx.channel()).sendKcpMessage(KcpBindAuthRsp.valueOf(false));
				ctx.channel().close();
				return;
			}


			PlayerActor playerActor = UserOnlineManager.getPlayerActor(kcpParamInfo.getPlayerId());
			ChannelUtil.getSession(ctx.channel()).addCloseListener("IKcpUsabilityLose", (session, cause) -> {
				playerActor.syncFireObserver(IKcpUsabilityChange.class, o -> o.ability(false));
			});
			// 老session踢下线
			if (playerActor.getSession().getKcpSession() != null) {
				playerActor.getSession().getKcpSession().close(CloseCause.LOGIN_REPEATED);
			}

			ChannelUtil.getSession(ctx.channel()).attachObj(ServerConstants.MESSAGE_ACTOR_KEY, playerActor);
			playerActor.getSession().bindKcpSession(((KcpSession) ChannelUtil.getSession(ctx.channel())));
			ChannelUtil.getSession(ctx.channel()).sendKcpMessage(KcpBindAuthRsp.valueOf(true));
			playerActor.asyncFireObserver(IKcpUsabilityChange.class, o -> o.ability(true));
			ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(playerActor);
			return;
		}

		// 没有鉴权
		if (content.getProtocolId() != IProtocolId.System.CONNECTION_REQ
		&& ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).get() == null) {
			ChannelUtil.getSession(ctx.channel()).sendKcpMessage(KcpBindAuthRsp.valueOf(false).buildChannelMessage());
			return;
		}

		ChannelUtil.channelRead(ctx.channel(), params, content);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ChannelUtil.cause(params.getStartupContext(), ctx.channel(), cause);
	}
}
