package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.netty.server.http.handler.HttpServerHandler;
import org.qiunet.flash.handler.netty.server.idle.NettyIdleCheckHandler;
import org.qiunet.flash.handler.netty.server.param.ServerBootStrapParam;
import org.qiunet.flash.handler.netty.server.tcp.handler.TcpServerHandler;

import java.util.Arrays;
import java.util.List;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class ChannelChoiceDecoder extends ByteToMessageDecoder {
	private final ServerBootStrapParam param;

	private ChannelChoiceDecoder(ServerBootStrapParam param) {
		this.param = param;
	}

	public static ChannelChoiceDecoder valueOf(ServerBootStrapParam param){
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
			if (Arrays.equals(protocolHeader.getConnectInMagic(), bytes)) {
				pipeline.addLast("TcpSocketEncoder", new TcpSocketServerEncoder());
				pipeline.addLast("TcpSocketDecoder", new TcpSocketServerDecoder(param.getMaxReceivedLength(), param.isEncryption()));
				pipeline.addLast("IdleStateHandler", new IdleStateHandler(param.getReadIdleCheckSeconds(), 0, 0));
				pipeline.addLast("NettyIdleCheckHandler", new NettyIdleCheckHandler());
				pipeline.addLast("TcpServerHandler", new TcpServerHandler(param));
				ctx.fireChannelActive();
			}else {
				pipeline.addLast("HttpServerCodec" ,new HttpServerCodec());
				pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(param.getMaxReceivedLength()));
				pipeline.addLast("HttpServerHandler", new HttpServerHandler(param));
			}
			pipeline.remove(ChannelChoiceDecoder.class);
		}finally {
			in.resetReaderIndex();
		}
	}
}