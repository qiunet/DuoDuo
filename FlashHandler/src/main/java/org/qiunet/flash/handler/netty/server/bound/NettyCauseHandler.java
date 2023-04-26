package org.qiunet.flash.handler.netty.server.bound;

import io.jpower.kcp.netty.KcpException;
import io.micrometer.core.instrument.Counter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.function.prometheus.RootRegistry;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.io.IOException;

/***
 * 最末端的 异常处理
 *
 * @author qiunet
 * 2023/4/20 10:38
 */
public class NettyCauseHandler extends ChannelDuplexHandler {
	private static final Counter counter = RootRegistry.instance.counter("project.exception");
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ISession session = ChannelUtil.getSession(ctx.channel());
		Channel channel = ctx.channel();
		Runnable closeChannel = () -> {
			if (session != null) {
				session.close(CloseCause.EXCEPTION);
			}else {
				channel.close();
			}
		};

		String errMeg = "Exception session ["+(session != null ? session.toString(): "null")+"]";
		if (cause instanceof KcpException || cause instanceof IOException) {
			logger.info(errMeg + " errMsg: " + cause.getMessage());
			closeChannel.run();
			return;
		}

		logger.error(errMeg, cause);
		counter.increment();
		if (channel.isOpen() || channel.isActive()) {
			ChannelUtil.exception(session, cause)
				.addListener(f -> {
					closeChannel.run();
				});
		}
	}
}
