package org.qiunet.flash.handler.netty.server.bound;

import io.jpower.kcp.netty.KcpException;
import io.micrometer.core.instrument.Counter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.status.StatusResultException;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerExceptionResponse;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.function.prometheus.RootRegistry;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.exceptions.CustomException;
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
	private static final LazyLoader<IChannelData> SERVER_EXCEPTION_MESSAGE = new LazyLoader<>(ServerExceptionResponse::new);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		ISession session = ChannelUtil.getSession(channel);
		String errMeg = "Session ["+(session != null ? session.toString(): "null")+"]";
		if (cause instanceof KcpException) {
			ChannelUtil.closeChannel(channel, CloseCause.EXCEPTION, "Kcp: {} err message: {}", errMeg, cause.getMessage());
			return;
		}

		if (cause instanceof IOException) {
			ChannelUtil.closeChannel(channel, CloseCause.EXCEPTION,"IO: {}, err message: {}", errMeg, cause.getMessage());
			return;
		}

		if (cause instanceof StatusResultException) {
			throw new CustomException("Should handler StatusResultException in IHandler!");
		}

		logger.error(errMeg, cause);
		counter.increment();
		if (session == null) {
			return;
		}

		if (channel.isOpen() || channel.isActive()) {
			session.sendMessage(SERVER_EXCEPTION_MESSAGE.get(), true).addListener(f -> {
					ChannelUtil.closeChannel(channel, CloseCause.EXCEPTION, "exception!");
				});
		}
	}
}
