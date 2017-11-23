package org.qiunet.flash.handler.context.request;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiunet.
 * 17/11/20
 */
public abstract class BaseRequestContext<RequestData> implements IRequestContext<RequestData> {
	protected MessageContent messageContent;
	protected ChannelHandlerContext ctx;
	protected byte [] bytes;

	private Map<String , Object> attributes;

	protected BaseRequestContext(MessageContent content, ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.bytes = content.bytes();
		this.messageContent = content;
	}
	@Override
	public IHandler<RequestData> getHandler() {
		return RequestHandlerMapping.getInstance().getGameHandler(messageContent.getProtocolId());
	}

	@Override
	public Object getAttribute(String key) {
		return attributes == null ? null : attributes.get(key);
	}
	@Override
	public void setAttribute(String key, Object val) {
		if (attributes == null) attributes = new HashMap<>();
		attributes.put(key, val);
	}
	@Override
	public int getSequence() {
		return messageContent.getSequence();
	}
}
