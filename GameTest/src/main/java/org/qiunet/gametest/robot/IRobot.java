package org.qiunet.gametest.robot;

import org.qiunet.enums.ProtocolType;
import org.qiunet.gametest.GameTest;
import org.qiunet.gametest.protocol.base.LoginProtocol;

/**
 *  T 是uid的类型
 * @author qiunet
 *         Created on 16/12/13 16:11.
 */
public interface IRobot {
	/***
	 * 获得 合作方的 唯一id, 一般字符串
	 * @return
	 */
	String getOpenid();
	/***
	 * 返回登录协议.
	 * @param type
	 * @return
	 */
	LoginProtocol login(ProtocolType type);

	/**
	 * 处理所有的普通协议.
	 * @param test
	 */
	void setGameTest(GameTest test);
}
