package org.qiunet.flash.handler.common.enums;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.utils.exceptions.CustomException;

/**
 * handler的类型. 区分使用
 * @author qiunet
 *         Created on 17/3/7 17:22.
 */
public enum HandlerType {
	/**
	 * 包括http  https
	 */
	HTTP,
	/**
	 * 长连接
	 * persistent connection
	 * 简称 PersistConn
	 */
	PERSIST_CONN {
		@Override
		public IRequestContext createRequestContext(IHandler handler, MessageContent content, Channel channel) {
			return handler.getDataType().createRequestContext(content, channel);
		}
	};

	/**
	 * 根据handlerType 创建requestContext
	 * @return
	 */
	public IRequestContext createRequestContext(IHandler handler, MessageContent content, Channel channel) {
		throw new CustomException("Not support!!");
	}
}
