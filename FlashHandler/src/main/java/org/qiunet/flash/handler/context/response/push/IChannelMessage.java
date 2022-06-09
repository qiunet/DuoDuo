package org.qiunet.flash.handler.context.response.push;

import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.utils.string.IDataToString;
import org.qiunet.utils.string.ToString;

import java.nio.ByteBuffer;

/**
 * 对外响应的编码消息
 * @author qiunet.
 * 17/12/11
 */
public interface IChannelMessage<T> {
	/***
	 * 得到消息的内容
	 * @return
	 */
	T getContent();

	/***
	 * 转换bytes
	 * @return
	 */
	ByteBuffer byteBuffer();
	/**
	 * 得到消息id
	 * @return
	 */
	int getProtocolID();
	/**
	 * 是否需要打印输出
	 * @return
	 */
	default boolean needLogger(){
		return ! this.getContent().getClass().isAnnotationPresent(SkipDebugOut.class);
	}

	/**
	 * 转logger 格式字符串
	 * @return
	 */
	default String toStr() {
		T content = this.getContent();
		if (IDataToString.class.isAssignableFrom(content.getClass())) {
			return ((IDataToString) content)._toString();
		}
		return ToString.toString(content);
	}
}
