package org.qiunet.test.server;

import org.qiunet.flash.handler.common.enums.HandlerType;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/12/6
 */
public interface IServer {
	/***
	 * 可能登录服  逻辑服  在线服 房间服  聊天服等..
	 * 所以一种HandlerTYpe 可能对应多种服
	 * @return
	 */
	public String getName();

	/***
	 * 得到类型 处理
	 * @return
	 */
	public HandlerType getType();
	/**
	 *
	 * @return
	 */
	public InetSocketAddress getAddress();
	/**
	 * 如果有则返回.
	 * @return
	 */
	public String uri();
}
