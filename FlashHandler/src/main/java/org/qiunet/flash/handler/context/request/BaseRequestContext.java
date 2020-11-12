package org.qiunet.flash.handler.context.request;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiunet.
 * 17/11/20
 */
public abstract class BaseRequestContext<RequestData> implements IRequestContext<RequestData> {
	protected Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	protected Channel channel;
	protected MessageContent messageContent;
	protected IHandler<RequestData> handler;
	private Map<String , Object> attributes;

	protected LazyLoader<RequestData> requestData = new LazyLoader<>(() -> {
		try {
			return getHandler().parseRequestData(messageContent.byteBuffer());
		}finally {
			messageContent.release();
		}
	});

	protected BaseRequestContext(MessageContent content, Channel channel) {
		this.channel = channel;
		this.messageContent = content;
		this.handler = RequestHandlerMapping.getInstance().getHandler(content);
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
