package org.qiunet.gametest.protocol;

import org.qiunet.enums.ProtocolType;
import org.qiunet.gametest.protocol.base.ISocketProtocol;
import org.qiunet.gametest.protocol.base.NormalProtocol;

/**
 * udp protocol 继承该类
 * @author qiunet
 *         Created on 16/12/14 10:13.
 */
public abstract class SocketUdpProtocol extends NormalProtocol implements ISocketProtocol {
	@Override
	public ProtocolType getProtocolType() {
		return ProtocolType.SOCKET_TCP;
	}
}
