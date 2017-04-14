package org.qiunet.gametest.protocol.base;

import org.qiunet.enums.ProtocolType;
import org.qiunet.gametest.protocol.data.DataParse;
import org.qiunet.gametest.robot.IRobot;

/**
 * @author qiunet
 *         Created on 16/12/13 16:39.
 */
public interface IProtocol {
	/**
	 * 处理上行数据
	 */
	DataParse request(IRobot robot);

	/**
	 * 处理响应数据
	 */
	void responseHandler(IRobot robot, DataParse data);
	/**
	 * 得到当前协议的 类型.
	 * @return
	 */
	ProtocolType getProtocolType();
	/**
	 * 返回处理DataParse的类
	 * @return
	 */
	Class<? extends DataParse> getResponseDataPaser();
}
