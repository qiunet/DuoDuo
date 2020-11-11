package org.qiunet.flash.handler.netty.server.tcp.handler;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.ICrossStatusActor;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
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
	private TcpBootstrapParams params;

	public TcpServerHandler(TcpBootstrapParams params) {
		this.params = params;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().attr(ServerConstants.HANDLER_TYPE_KEY).set(HandlerType.TCP);
		DSession session = new DSession(ctx.channel());

		ChannelUtil.bindSession(session);
		ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY).set(params.getStartupContext().buildMessageActor(session));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageContent content = ((MessageContent) msg);
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(content);
		if (handler == null) {
			ctx.writeAndFlush(params.getStartupContext().getHandlerNotFound());
			return;
		}
		DSession session = ChannelUtil.getSession(ctx.channel());
		Preconditions.checkNotNull(session);

		IMessageActor messageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (handler instanceof ITransmitHandler
			&& messageActor instanceof ICrossStatusActor
			&& ((ICrossStatusActor) messageActor).isCrossStatus()) {
			((ICrossStatusActor) messageActor).crossSession().writeMessage(TransmitRequest.valueOf(content.getProtocolId(), content.bytes()));
			return;
		}
		if (ctx.channel().isActive()) {
			ITcpRequestContext context = handler.getDataType().createTcpRequestContext(content, ctx.channel(), handler, messageActor);
			messageActor.addMessage(context);
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
