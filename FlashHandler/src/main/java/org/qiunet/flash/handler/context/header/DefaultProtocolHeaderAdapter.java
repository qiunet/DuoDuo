package org.qiunet.flash.handler.context.header;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.server.param.adapter.IProtocolHeaderAdapter;

import java.nio.ByteBuffer;

public class DefaultProtocolHeaderAdapter implements IProtocolHeaderAdapter {
	@Override
	public IProtocolHeader newHeader(ByteBuf in) {
		return new DefaultProtocolHeader(in);
	}

	@Override
	public IProtocolHeader newHeader(ByteBuffer in) {
		return new DefaultProtocolHeader(in);
	}

	@Override
	public IProtocolHeader newHeader(MessageContent content) {
		return new DefaultProtocolHeader(content);
	}

	@Override
	public int getHeaderLength() {
		return DefaultProtocolHeader.REQUEST_HEADER_LENGTH;
	}
}
