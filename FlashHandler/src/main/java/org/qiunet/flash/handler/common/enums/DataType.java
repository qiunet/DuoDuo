package org.qiunet.flash.handler.common.enums;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.http.HttpJsonRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpPbRequestContext;
import org.qiunet.flash.handler.context.request.http.HttpStringRequestContext;
import org.qiunet.flash.handler.context.request.persistconn.PersistConnPbRequestContext;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.handler.IHandler;
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
		public IRequestContext createRequestContext(ISession session, MessageContent content) {
			return new HttpStringRequestContext(session, content);
		}
	},
	/**
	 * protobuf
	 */
	PROTOBUF {

		@Override
		public IRequestContext createRequestContext(ISession session, MessageContent content) {
			IHandler handler = ChannelDataMapping.getHandler(content.getProtocolId());
			if (handler.getHandlerType() == HandlerType.HTTP) {
				return new HttpPbRequestContext(session, content);
			}else {
				return PersistConnPbRequestContext.valueOf(session, content);
			}

		}
	},
	/**
	 * json
	 */
	JSON {
		@Override
		public IRequestContext createRequestContext(ISession session, MessageContent content) {
			return new HttpJsonRequestContext(session, content);
		}
	}
	;
	/**
	 * 得到一个 request context
	 * @param content
	 * @return
	 */
	public IRequestContext createRequestContext(ISession session, MessageContent content) {
		throw new CustomException("Not support!!");
	}
}
