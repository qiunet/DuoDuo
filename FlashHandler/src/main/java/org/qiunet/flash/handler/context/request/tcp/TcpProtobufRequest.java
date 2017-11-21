package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.header.MessageContent;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class TcpProtobufRequest<RequestData> extends AbstractTcpRequest<RequestData> {
	private RequestData requestData;
	public TcpProtobufRequest(MessageContent content, ChannelHandlerContext channelContext) {
		super(content, channelContext);
	}

	@Override
	public RequestData getRequestData() {
		if (requestData != null) return requestData;
		try {
			this.requestData = getHandler().parseRequestData(bytes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return requestData;
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
