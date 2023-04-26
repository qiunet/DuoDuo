package org.qiunet.flash.handler.context.header;

/***
 *  跨服响应头
 * @author qiunet
 * 2023/4/26 11:23
 */
public interface INodeServerHeader {
	/**
	 * 是否是 服务间 通讯
	 * @return true 是
	 */
	boolean isServerNodeMsg();
	/**
	 *  是否是 玩家的通讯
	 * @return true 是
	 */
	boolean isPlayerMsg();
	/**
	 * 得到对面的serverId
	 * @return
	 */
	int getServerId();
	/**
	 * 是否flush
	 * @return
	 */
	boolean isFlush();
	/**
	 *  是否是kcp消息
	 * @return
	 */
	boolean isKcp();
	/**
	 * 对应的id
	 * @return
	 */
	long id();
}
