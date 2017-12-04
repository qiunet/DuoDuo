package org.qiunet.test.robot;

/**
 * 机器人的接口
 * Created by qiunet.
 * 17/11/24
 */
public interface IRobot {
	/***
	 *
	 * @return
	 */
	int getUid();

	void setUid(int uid);

	String getOpenId();

	void setOpenId(String openId);

}
