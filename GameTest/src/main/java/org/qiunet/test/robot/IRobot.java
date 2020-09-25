package org.qiunet.test.robot;


import org.qiunet.test.robot.init.IRobotInitInfo;

/**
 * 机器人的接口
 * Created by qiunet.
 * 17/11/24
 */
public interface IRobot<Info extends IRobotInitInfo> extends IRobotFunc {
	/**
	 * 运行case
	 * @return
	 */
	boolean runCases();
	/***
	 *
	 * @return
	 */
	int getUid();

	/**
	 * 得到token
	 * @return
	 */
	String getToken();
	/**
	 * 登录后设置 uid  token
	 * @param uid
	 * @param token
	 */
	void setUidAndToken(int uid, String token);
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
}
