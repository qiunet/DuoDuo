package org.qiunet.flash.handler.context.request.tcp;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class TcpProtobufRequestContext<RequestData> extends AbstractTcpRequestContext<RequestData, GeneratedMessageV3> {
	private RequestData requestData;
	public TcpProtobufRequestContext(MessageContent content, ChannelHandlerContext channelContext, TcpBootstrapParams params) {
		super(content, channelContext, params);
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
	protected byte[] getResponseDataBytes(GeneratedMessageV3 generatedMessageV3) {
		return generatedMessageV3.toByteArray();
	}

	@Override
	public boolean handler() {
		FacadeTcpRequest<RequestData> facadeTcpRequest = new FacadeTcpRequest<>(this);
		params.getInterceptor().handler((ITcpHandler) getHandler(), facadeTcpRequest);
		return true;
	}

	@Override
	public String toStr() {
		return null;
	}
}
