package org.qiunet.flash.handler.netty.server.tcp.handler;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.ICrossStatusActor;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataMapping;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequestContext;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.transmit.ITransmitHandler;
import org.qiunet.flash.handler.netty.transmit.TransmitRequest;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;


/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private final TcpBootstrapParams params;

	public TcpServerHandler(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(ServerConnType.TCP);
		DSession session = new DSession(ctx.channel());

		ChannelUtil.bindSession(session);
		ctx.channel().attr(ServerConstants.HANDLER_PARAM_KEY).set(params);
		ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(params.getStartupContext().buildMessageActor(session));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageContent content = ((MessageContent) msg);
		if (content.getProtocolId() == IProtocolId.System.CLIENT_PING) {
			ctx.writeAndFlush(params.getStartupContext().serverPongMsg());
			return;
		}

		IHandler handler = PbChannelDataMapping.getHandler(content.getProtocolId());
		if (handler == null) {
			ctx.writeAndFlush(params.getStartupContext().getHandlerNotFound());
			content.release();
			return;
		}
		DSession session = ChannelUtil.getSession(ctx.channel());
		Preconditions.checkNotNull(session);

		IMessageActor messageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (handler instanceof ITransmitHandler
			&& messageActor instanceof ICrossStatusActor
			&& ((ICrossStatusActor) messageActor).isCrossStatus()) {
			((ICrossStatusActor) messageActor).crossSession().sendMessage(TransmitRequest.valueOf(content.getProtocolId(), content.bytes()));
			return;
		}
		if (ctx.channel().isActive()) {
			IPersistConnRequestContext context = handler.getDataType().createPersistConnRequestContext(content, ctx.channel(), handler, messageActor);
			messageActor.addMessage(context);
		}else{
			content.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		DSession session = ChannelUtil.getSession(ctx.channel());
		String errMeg = "Exception session ["+(session != null ? session.toString(): "null")+"]";
		logger.error(errMeg, cause);

		if (ctx.channel().isOpen() || ctx.channel().isActive()) {
			ctx.writeAndFlush(params.getStartupContext().exception(cause)).addListener(ChannelFutureListener.CLOSE);
			if (session != null) {
				session.close(CloseCause.EXCEPTION);
			}else {
				ctx.close();
			}
		}
	}
}
