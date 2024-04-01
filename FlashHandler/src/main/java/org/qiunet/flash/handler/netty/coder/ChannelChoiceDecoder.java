package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.ScheduledFuture;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.netty.server.bound.*;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.http.handler.HttpServerHandler;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class ChannelChoiceDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	private static final byte[] POST_BYTES = {'P', 'O', 'S', 'T'};
	private static final byte[] HEAD_BYTES = {'H', 'E', 'A', 'D'};
	private static final byte[] GET_BYTES = {'G', 'E', 'T'};
	private ScheduledFuture<?> closeFuture;

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		this.closeFuture = ctx.channel().eventLoop().schedule(() -> {
			// 关闭那些只连接. 不发送任何协议的客户端
			ChannelUtil.closeChannel(ctx.channel(), CloseCause.INACTIVE, "20秒没有消息!");
		}, 20, TimeUnit.SECONDS);
		super.channelRegistered(ctx);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		ServerBootStrapConfig config = ctx.channel().attr(ServerConstants.BOOTSTRAP_CONFIG_KEY).get();
		IProtocolHeader protocolHeader = config.getProtocolHeader();

		ChannelPipeline pipeline = ctx.channel().pipeline();
		if (equals(protocolHeader.getConnectInMagic(), in)) {
			pipeline.addLast("InvalidChannelCleanHandler", new InvalidChannelCleanHandler());
			pipeline.addLast("TcpSocketEncoder", new TcpSocketServerEncoder(config.getMaxMsgLength()));
			pipeline.addLast("TcpSocketDecoder", new TcpSocketServerDecoder(config.getMaxMsgLength(), config.isEncryption()));
			pipeline.addLast("IdleStateHandler", new IdleStateHandler(config.getReadIdleCheckSeconds(), 0, 0));
			pipeline.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler());
			protocolHeader.completeServerHandler(in, pipeline);
			pipeline.addLast("MessageReadHandler", new MessageReadHandler());
			pipeline.addLast("FlushBalanceHandler", new FlushBalanceHandler());
			// 需要先移除. 增加到pipeline最后
			pipeline.remove(NettyCauseHandler.class);
			pipeline.addLast("NettyCauseHandler", new NettyCauseHandler());
			ctx.fireChannelActive();
		}else if (! config.isBanHttpServer()
				&& (equals(POST_BYTES, in) || equals(GET_BYTES, in) || equals(HEAD_BYTES, in))){
			pipeline.addLast("HttpServerCodec" ,new HttpServerCodec());
			pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(config.getMaxMsgLength()));
			pipeline.addLast("HttpServerHandler", new HttpServerHandler(config));
			ctx.fireChannelActive();
		}else {
			// 包头不够. 等待后面粘包
			return;
		}
		pipeline.remove(ChannelChoiceDecoder.class);
		closeFuture.cancel(true);
	}

	/**
	 * 对比数组. 只要符合origin即可
	 * @param origin
	 * @param in
	 * @return
	 */
	private boolean equals(byte [] origin, ByteBuf in) {
		if (in.readableBytes() < origin.length) {
			return false;
		}

		for (int i = 0; i < origin.length; i++) {
			if (in.getByte(i) != origin[i]) {
				return false;
			}
		}
		return true;
	}
}
