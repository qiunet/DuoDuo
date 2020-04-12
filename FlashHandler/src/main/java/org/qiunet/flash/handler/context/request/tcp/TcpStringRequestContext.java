package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.response.push.DefaultStringMessage;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.utils.string.StringUtil;

/**
 * tcp 请求解析成字符串的.
 * Created by qiunet.
 * 17/11/21
 */
public class TcpStringRequestContext<P extends IPlayerActor> extends AbstractTcpRequestContext<String, String, P> {
	protected String requestData;
	public TcpStringRequestContext(MessageContent content, ChannelHandlerContext channelContext, P playerActor) {
		super(content, channelContext, playerActor);
	}

	@Override
	protected IResponseMessage getResponseMessage(int protocolId, String s) {
		return new DefaultStringMessage(protocolId, s);
	}

	@Override
	public String getRequestData() {
		if (StringUtil.isEmpty(requestData)) {
			this.requestData = getHandler().parseRequestData(messageContent.bytes());
		}
		return requestData;
	}

	@Override
	public void handlerRequest() {
		FacadeTcpRequest<String, String, P> facadeTcpRequest = new FacadeTcpRequest<>(this);
		try {
			((ITcpHandler) getHandler()).handler(playerActor, facadeTcpRequest);
		} catch (Exception e) {
			logger.error("TcpStringRequestContext Exception", e);
		}
	}

	@Override
	public void execute(P p) {
		this.handlerRequest();
	}
}
