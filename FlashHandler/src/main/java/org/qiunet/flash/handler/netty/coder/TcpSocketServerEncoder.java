package org.qiunet.flash.handler.netty.coder;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpSocketServerEncoder extends BasicTcpSocketCoder {

	public TcpSocketServerEncoder(int maxDecodeReceivedLength) {
		super(maxDecodeReceivedLength);
	}
}
