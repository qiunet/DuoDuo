package org.qiunet.gametest.robot;

import org.apache.mina.core.session.IoSession;

/**
 * 如果有tcp socket 的协议, 需要实现该接口
 * @author qiunet
 *         Created on 16/12/15 09:43.
 */
public interface TcpRobot {
	/**
	 * 获得tcpsession
	 * @return
	 */
	IoSession getTcpSession();
	/**
	 * 设置IoSession
	 * @param ioSession
	 */
	void setTcpIoSession(IoSession ioSession);
}
