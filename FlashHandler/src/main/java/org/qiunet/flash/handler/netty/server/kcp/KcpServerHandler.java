package org.qiunet.flash.handler.netty.server.kcp;

import io.jpower.kcp.netty.UkcpChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.KcpSession;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.mapping.KcpPlayerTokenMapping;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpBindAuthReq;
import org.qiunet.flash.handler.netty.server.kcp.shakehands.message.KcpBindAuthRsp;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Objects;

/***
 *
 * @author qiunet
 * 2022/4/25 18:19
 */
public class KcpServerHandler extends SimpleChannelInboundHandler<MessageContent> {

	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final ServerBootStrapConfig config;

	public KcpServerHandler(ServerBootStrapConfig config) {
		this.config = config;
	}



	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ISession session;

		if (! config.getKcpBootstrapConfig().isDependOnTcpWs()) {
			session = new DSession(ctx.channel());
			((DSession) session).bindKcpSession(new KcpSession(ctx.channel()));
			// Kcp如果不依赖Tcp, 也是直接使用PlayerActor
			session.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, new PlayerActor(session));
		}else {
			 session = new KcpSession(ctx.channel());
		}
		ChannelUtil.bindSession(session, ctx.channel());

		ChannelUtil.getSession(ctx.channel()).attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.KCP);
		session.attachObj(ServerConstants.BOOTSTRAP_CONFIG_KEY, config);
		logger.debug("Kcp session {} active!", session);
		ctx.fireChannelActive();
	}

	/**
	 *  负责绑定Kcp session 到playerActor
	 * @param ctx
	 * @param content
	 * @return  false 继续执行 true 终止.
	 */
	private boolean handlerBind(ChannelHandlerContext ctx, MessageContent content) {
		// 鉴权协议. 用来绑定PlayerActor
		if (content.getProtocolId() != IProtocolId.System.KCP_BIND_AUTH_REQ) {
			return false;
		}

		if (! config.getKcpBootstrapConfig().isDependOnTcpWs()) {
			return false;
		}

		KcpBindAuthReq req = ProtobufDataManager.decode(KcpBindAuthReq.class, content.byteBuffer());
		if (logger.isInfoEnabled()) {
			logger.info("[{}({})] <<< {}", "KCP", ctx.channel().id().asShortText(), req._toString());
		}

		KcpPlayerTokenMapping kcpParamInfo = KcpPlayerTokenMapping.getPlayer(req.getPlayerId());
		ISession kcpSession = ChannelUtil.getSession(ctx.channel());
		if (kcpParamInfo == null
				|| ! Objects.equals(req.getToken(), kcpParamInfo.getToken())
				|| ((UkcpChannel) ctx.channel()).conv() != kcpParamInfo.getConvId()
		) {
			if (kcpParamInfo == null) {
				logger.error("ID: {} kcpParamInfo null, is online: {}", req.getPlayerId(), UserOnlineManager.instance.getPlayerActor(req.getPlayerId()) == null);
			}else if (! Objects.equals(req.getToken(), kcpParamInfo.getToken())) {
				logger.error("ID: {} token error, {} and {}", req.getPlayerId(), req.getToken(), kcpParamInfo.getToken());
			}
			kcpSession.sendMessage(KcpBindAuthRsp.valueOf(false), true);
			ctx.channel().close();
			return true;
		}

		PlayerActor playerActor = UserOnlineManager.instance.getPlayerActor(kcpParamInfo.getPlayerId());
		playerActor.getSession().bindKcpSession(((KcpSession) kcpSession));
		kcpSession.sendMessage(KcpBindAuthRsp.valueOf(true), true);
		kcpSession.attachObj(ServerConstants.MESSAGE_ACTOR_KEY, playerActor);
		return false;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
		if (ChannelUtil.handlerPing(ctx.channel(), content) || this.handlerBind(ctx, content)) {
			return;
		}

		ISession session = ChannelUtil.getSession(ctx.channel());
		// 没有鉴权
		if (content.getProtocolId() != IProtocolId.System.CONNECTION_REQ
		&& session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY) == null) {
			session.sendMessage(KcpBindAuthRsp.valueOf(false), true);
			return;
		}

		ChannelUtil.channelRead(ctx.channel(), config, content);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ChannelUtil.cause(config.getStartupContext(), ctx.channel(), cause);
	}
}
