package org.qiunet.flash.handler.netty.server.bound;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/***
 * 无效的连接清理.
 * 如果客户端连接上来. 30秒没有鉴权.
 * 就close 这个连接.
 *
 * @author qiunet
 * 2023/5/9 14:13
 */
public class InvalidChannelCleanHandler extends ChannelDuplexHandler {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		ctx.executor().schedule(() -> {
			Attribute<IMessageActor> actorAttribute = ctx.channel().attr(ServerConstants.MESSAGE_ACTOR_KEY);
			// DTools 工具的不断开
			if (actorAttribute != null
				&& (Objects.equals(actorAttribute.get().msgExecuteIndex(), "DTools") || actorAttribute.get().isAuth())) {
				ctx.channel().pipeline().remove(this);
				return;
			}

			ISession session = ChannelUtil.getSession(ctx.channel());
			if (session != null) {
				session.close(CloseCause.INVALID_CHANNEL);
			}else {
				logger.error("Channel [{}] close by invalid channel clear!", ctx.channel().id().asShortText());
				ctx.channel().close();
			}
		}, 30, TimeUnit.SECONDS);
	}
}
