package org.qiunet.event.log.message;

/**
 * 日志消息
 */
interface IMessage {
	/**
	 * 发送到日志记录器.
	 */
	void send();
}
