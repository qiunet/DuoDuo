package org.qiunet.flash.handler.context.response.push;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.IDataToString;
import org.qiunet.utils.json.JsonUtil;

/**
 * 对外响应的编码消息
 * @author qiunet.
 * 17/12/11
 */
public interface IChannelMessage<T> {
	/***
	 * push 消息 编码
	 * @return
	 */
	MessageContent encode();

	/***
	 * 得到消息的内容
	 * @return
	 */
	T getContent();

	/***
	 * 转换bytes
	 * @return
	 */
	byte[] bytes();
	/**
	 * 得到消息id
	 * @return
	 */
	int getProtocolID();

	/**
	 * 转logger 格式字符串
	 * @return
	 */
	default String toStr() {
		T content = this.getContent();
		if (content instanceof IDataToString) {
			return ((IDataToString) content)._toString();
		}
		return content.getClass().getSimpleName() + "" +JsonUtil.toJsonString(content);
	}
}
