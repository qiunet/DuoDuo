package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.string.StringUtil;

/**
 * tcp 请求解析成字符串的.
 * Created by qiunet.
 * 17/11/21
 */
public class TcpStringRequestContext<P extends IPlayerActor> extends AbstractTcpRequestContext<String, P> {
	private LazyLoader<String> requestData = new LazyLoader<>(() -> getHandler().parseRequestData(messageContent.bytes()));

	public TcpStringRequestContext(MessageContent content, ChannelHandlerContext channelContext, P playerActor) {
		super(content, channelContext, playerActor);
	}

	@Override
	public String getRequestData() {
		return requestData.get();
	}

	@Override
	public void handlerRequest() {
		FacadeTcpRequest<String, P> facadeTcpRequest = new FacadeTcpRequest<>(this);
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
