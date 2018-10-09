package org.qiunet.flash.handler.context.request.udp;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.DefaultStringMessage;
import org.qiunet.flash.handler.context.response.push.IMessage;
import org.qiunet.flash.handler.handler.udp.IUdpHandler;
import org.qiunet.flash.handler.netty.server.param.UdpBootstrapParams;
import org.qiunet.flash.handler.netty.server.udp.handler.UdpChannel;
import org.qiunet.utils.string.StringUtil;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/10/1 20:00
 **/
public class UdpProtobufRequestContext<RequestData> extends AbstractUdpRequestContext<RequestData, GeneratedMessageV3> {
	protected RequestData requestData;
	public UdpProtobufRequestContext(MessageContent content, UdpChannel channel, UdpBootstrapParams params) {
		super(content, channel, params);
	}

	@Override
	protected IMessage getResponseMessage(int protocolId, GeneratedMessageV3 responseObject) {
		return new DefaultProtobufMessage(protocolId, responseObject);
	}

	@Override
	public RequestData getRequestData() {
		if (requestData != null) return requestData;
		this.requestData = getHandler().parseRequestData(messageContent.bytes());
		return requestData;
	}

	@Override
	public boolean handler() {
		FacadeUdpRequest<String> facadeTcpRequest = new FacadeUdpRequest(this);
		params.getUdpInterceptor().handler((IUdpHandler) getHandler(), facadeTcpRequest);
		return true;
	}

	@Override
	public String toStr() {
		return null;
	}
}
