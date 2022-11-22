package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.http.handler.HttpServerHandler;
import org.qiunet.flash.handler.netty.server.idle.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.tcp.handler.TcpServerHandler;

import java.util.Arrays;
import java.util.List;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class ChannelChoiceDecoder extends ByteToMessageDecoder {
	private final ServerBootStrapConfig config;

	private ChannelChoiceDecoder(ServerBootStrapConfig config) {
		this.config = config;
	}

	public static ChannelChoiceDecoder valueOf(ServerBootStrapConfig param){
		return new ChannelChoiceDecoder(param);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		IProtocolHeader protocolHeader = ctx.channel().attr(ServerConstants.PROTOCOL_HEADER).get();
		if (in.readableBytes() < protocolHeader.getConnectInMagic().length) {
			return;
		}
		in.markReaderIndex();
		try {
			ChannelPipeline pipeline = ctx.channel().pipeline();
			byte [] bytes = new byte[protocolHeader.getConnectInMagic().length];
			in.readBytes(bytes);
			if (config.isBanHttpServer() || Arrays.equals(protocolHeader.getConnectInMagic(), bytes)) {
				pipeline.addLast("TcpSocketEncoder", new TcpSocketServerEncoder());
				pipeline.addLast("TcpSocketDecoder", new TcpSocketServerDecoder(config.getMaxReceivedLength(), config.isEncryption()));
				pipeline.addLast("IdleStateHandler", new IdleStateHandler(config.getReadIdleCheckSeconds(), 0, 0));
				pipeline.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler());
				pipeline.addLast("TcpServerHandler", new TcpServerHandler(config));
				ctx.fireChannelActive();
			}else {
				pipeline.addLast("HttpServerCodec" ,new HttpServerCodec());
				pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(config.getMaxReceivedLength()));
				pipeline.addLast("HttpServerHandler", new HttpServerHandler(config));
			}
			pipeline.remove(ChannelChoiceDecoder.class);
		}finally {
			in.resetReaderIndex();
		}
	}
}
