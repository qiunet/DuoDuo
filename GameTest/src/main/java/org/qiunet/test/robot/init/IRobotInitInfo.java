package org.qiunet.test.robot.init;

/**
 *  机器人初始化的信息.
 *
 * Created by qiunet.
 * 17/12/5
 */
public interface IRobotInitInfo {
	/**
	 * 得到Openid
	 * @return
	 */
	String getOpenId();

	/***
	 * 得到sid
	 * @return
	 */
	String getSid();
}
