package org.qiunet.gametest.robot;

import org.apache.mina.core.session.IoSession;

/**
 * 如果有 udp的协议. 需要实现该接口
 * @author qiunet
 *         Created on 16/12/15 09:44.
 */
public interface UdpRobot {
	/**
	 * 获得udp的session
	 * @return
	 */
	IoSession getUdpSession();
	/**
	 * 设置IoSession
	 * @param ioSession
	 */
	void setUdpIoSession(IoSession ioSession);
}