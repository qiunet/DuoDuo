package org.qiunet.gametest.protocol.base;
/**
 * socket 要求有cmdid
 * @author qiunet
 *         Created on 16/12/15 12:04.
 */
public interface ISocketProtocol {
	/**
	 * 要求有cmdid
	 * @return
	 */
	int getCommandId();
}
