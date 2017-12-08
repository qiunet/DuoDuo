package org.qiunet.test.robot;

import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.test.robot.init.IRobotInitInfo;
import org.qiunet.test.server.IServer;

/**
 * 机器人的接口
 * Created by qiunet.
 * 17/11/24
 */
public interface IRobot<Info extends IRobotInitInfo> extends Runnable {
	/***
	 *
	 * @return
	 */
	int getUid();
	/**
	 *
	 * @return
	 */
	Info getRobotInitInfo();
	/**
	 * 某种理由中断机器人. 后续的操作不执行
	 * 一般是服务器返回登录不成功 创建角色不成功等.
	 */
	void brokeRobot(String brokeReason);

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
