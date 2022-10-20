package org.qiunet.flash.handler.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.header.IProtocolHeader;

/**
 * 消息的解析
 * Created by qiunet.
 * 17/8/13
 */
public class WebSocketClientDecoder extends WebSocketServerDecoder {
	public WebSocketClientDecoder(int maxReceivedLength, boolean encryption) {
		super(maxReceivedLength, encryption);
	}

	@Override
	public int getHeaderLength(IProtocolHeader protocolHeader, boolean connectReq) {
		return protocolHeader.getClientInHeadLength();
	}

	@Override
	public IProtocolHeader.ProtocolHeader getHeader(IProtocolHeader protocolHeader, ByteBuf in, Channel channel, boolean connectReq) {
		return protocolHeader.clientNormalIn(in, channel);
	}
}
