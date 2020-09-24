package org.qiunet.cross;

/***
 * 跨服数据传输.
 * 业务自己实现
 * @author qiunet
 * 2020-09-24 09:36
 */
public interface ITansferSender {
	/**
	 * 发送数据
	 * @param serverId 服务器id
	 * @param data 数据
	 */
	void send(int serverId, Object data);
}
