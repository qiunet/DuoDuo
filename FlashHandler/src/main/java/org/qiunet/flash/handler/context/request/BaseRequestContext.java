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
	private IHandler<RequestData> handler;
	private Map<String , Object> attributes;

	protected BaseRequestContext(MessageContent content, ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.bytes = content.bytes();
		this.messageContent = content;
		if (content.getProtocolId() > 0) {
			this.handler = RequestHandlerMapping.getInstance().getGameHandler(messageContent.getProtocolId());
		}else {

			this.handler = RequestHandlerMapping.getInstance().getOtherRequestHandler(messageContent.getUriPath());
		}
	}
	@Override
	public IHandler<RequestData> getHandler() {
		return handler;
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
}
