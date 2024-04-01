package org.qiunet.flash.handler.netty.coder;

/**
 * Created by qiunet.
 * 17/8/13
 */
public class TcpSocketClientEncoder extends BasicTcpSocketCoder {

	public TcpSocketClientEncoder(int maxDecodeReceivedLength) {
		super(maxDecodeReceivedLength);
	}
}
