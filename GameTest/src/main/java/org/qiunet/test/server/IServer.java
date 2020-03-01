package org.qiunet.test.server;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.netty.client.param.IClientConfig;

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
	String getName();

	/**
	 * 得到端口
	 * @return
	 */
	int port();
	/**
	 * 得到地址
	 * @return
	 */
	String host();
	/***
	 * 得到类型 处理
	 * @return
	 */
	HandlerType getType();
	/**
	 * 得到请求的地址 socket 则只有host  port 可用
	 * @return
	 */
	IClientConfig getClientConfig();
}
