package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.header.MessageContent;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class TcpProtobufRequest<RequestData> extends AbstractTcpRequest<RequestData> {

	public TcpProtobufRequest(MessageContent content, ChannelHandlerContext channelContext) {
		super(content, channelContext);
	}

	@Override
	public RequestData getRequestData() {
		return null;
	}

	@Override
	public void response(int protocolId, Object o) {

	}

	@Override
	public boolean handler() {
		return false;
	}

	@Override
	public String toStr() {
		return null;
	}
}
