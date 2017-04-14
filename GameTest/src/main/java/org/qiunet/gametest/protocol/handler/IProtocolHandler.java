package org.qiunet.gametest.protocol.handler;

import org.qiunet.enums.ProtocolType;
import org.qiunet.gametest.protocol.base.IProtocol;
import org.qiunet.gametest.robot.IRobot;

/**
 * 协议的处理
 * @author qiunet
 *         Created on 16/12/13 15:52.
 */
public interface IProtocolHandler {
	/**
	 * 负责实现登录
	 * 把登录返回的值 set 到robot
	 * @param robot
	 * @return true 登录没有问题, 否则不能进行下一步.
	 */
	boolean login(IRobot robot);
	/**
	 * 处理协议
	 * @param protocol
	 * @param robot
	 * @return
	 */
	boolean handler(IProtocol protocol, IRobot robot);
	/**
	 * 关闭session
	 * @param robot
	 * @return
	 */
	boolean closeSession(IRobot robot);
	/**
	 * 得到协议处理器类型
	 * @return
	 */
	ProtocolType getType();
}
