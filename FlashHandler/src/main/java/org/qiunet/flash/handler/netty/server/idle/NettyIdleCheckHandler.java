package org.qiunet.flash.handler.netty.server.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/***
 * 检查 如果超时 , 关闭channel
 */
public class NettyIdleCheckHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
			ISession session = ChannelUtil.getSession(ctx.channel());
			logger.info("[NettyIdleCheckHandler] Session [{}] Channel [{}] close by idle check", session, ctx
			.channel().id());
			if (session != null) {
				session.close(CloseCause.CHANNEL_IDLE);
			}else {
				ctx.channel().close();
			}
		}
	}
}
