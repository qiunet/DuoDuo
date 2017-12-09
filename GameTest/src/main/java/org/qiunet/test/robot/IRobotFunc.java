package org.qiunet.test.robot;

import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.test.server.IServer;

/**
 * Created by qiunet.
 * 17/12/9
 */
public interface IRobotFunc {

	/**
	 * 阻塞等待某个响应id
	 * @param id
	 */
	void parkForResponseID(int id);
	/***
	 * 得到长连接客户端的对象
	 * @param Server
	 * @return
	 */
	ILongConnClient getLongConnClient(IServer Server);
}
