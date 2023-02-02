package org.qiunet.flash.handler.common.enums;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.http.HttpJsonRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpPbRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpStringRequestContext;
import org.qiunet.flash.handler.context.request.persistconn.PersistConnPbRequestContext;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.exceptions.CustomException;

/**
 * 传输使用的数据类型
 * Created by qiunet.
 * 17/11/21
 */
public enum DataType {
	/***
	 * 字符串
	 */
	STRING {
		@Override
		public IRequestContext createRequestContext(MessageContent content, Channel channel) {
			return new HttpStringRequestContext(content, channel, channel.attr(ServerConstants.BOOTSTRAP_CONFIG_KEY).get(), channel.attr(ServerConstants.HTTP_REQUEST_KEY).get());
		}
	},
	/**
	 * protobuf
	 */
	PROTOBUF {

		@Override
		public IRequestContext createRequestContext(MessageContent content, Channel channel) {
			IHandler handler = ChannelDataMapping.getHandler(content.getProtocolId());
			if (handler.getHandlerType() == HandlerType.HTTP) {
				return new HttpPbRequestContext(content, channel, channel.attr(ServerConstants.BOOTSTRAP_CONFIG_KEY).get(), channel.attr(ServerConstants.HTTP_REQUEST_KEY).get());
			}else {
				return PersistConnPbRequestContext.valueOf(content, channel, channel.attr(ServerConstants.MESSAGE_ACTOR_KEY).get());
			}

		}
	},
	/**
	 * json
	 */
	JSON {
		@Override
		public IRequestContext createRequestContext(MessageContent content, Channel channel) {
			return new HttpJsonRequestContext(content, channel, channel.attr(ServerConstants.BOOTSTRAP_CONFIG_KEY).get(), channel.attr(ServerConstants.HTTP_REQUEST_KEY).get());
		}
	}
	;
	/**
	 * 得到一个 request context
	 * @param content
	 * @param channel
	 * @return
	 */
	public IRequestContext createRequestContext(MessageContent content, Channel channel) {
		throw new CustomException("Not support!!");
	}
}
