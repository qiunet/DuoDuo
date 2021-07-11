package org.qiunet.game.test.server;

import org.qiunet.flash.handler.common.enums.ServerConnType;
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
	String name();

	/**
	 * 得到端口
	 * @return
	 */
	default int port(){
		return getClientConfig().getAddress().getPort();
	}
	/**
	 * 得到地址
	 * @return
	 */
	default String host(){
		return getClientConfig().getAddress().getHostString();
	}
	/***
	 * 得到类型 处理
	 * @return
	 */
	default ServerConnType getType() {
		return getClientConfig().getConnType();
	}
	/**
	 * 得到请求的地址 socket 则只有host  port 可用
	 * @return
	 */
	IClientConfig getClientConfig();
}
