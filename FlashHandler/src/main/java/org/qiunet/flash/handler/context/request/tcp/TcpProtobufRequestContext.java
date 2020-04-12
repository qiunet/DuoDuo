package org.qiunet.flash.handler.context.request.tcp;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class TcpProtobufRequestContext<RequestData, P extends IPlayerActor> extends AbstractTcpRequestContext<RequestData, GeneratedMessageV3, P> {
	private RequestData requestData;
	public TcpProtobufRequestContext(MessageContent content, ChannelHandlerContext channelContext, P plyaerActor) {
		super(content, channelContext, plyaerActor);
	}

	@Override
	public RequestData getRequestData() {
		if (requestData != null) return requestData;
		this.requestData = getHandler().parseRequestData(messageContent.bytes());
		return requestData;
	}

	@Override
	protected IResponseMessage getResponseMessage(int protocolId, GeneratedMessageV3 generatedMessageV3) {
		return new DefaultProtobufMessage(protocolId, generatedMessageV3);
	}

	@Override
	public void execute(P p) {
		this.handlerRequest();
	}

	@Override
	public void handlerRequest() {
		FacadeTcpRequest<RequestData, GeneratedMessageV3, P> facadeTcpRequest = new FacadeTcpRequest<>(this);
		try {
			((ITcpHandler) getHandler()).handler(playerActor, facadeTcpRequest);
		} catch (Exception e) {
			logger.error("TcpProtobufRequestContext Exception:", e);
		}
	}
}
