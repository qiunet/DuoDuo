package org.qiunet.flash.handler.netty.coder;

/***
 *
 *
 * @author qiunet
 * 2022/4/25 15:00
 */
public class KcpSocketClientEncoder extends TcpSocketClientEncoder {
	public KcpSocketClientEncoder(int maxDecodeReceivedLength) {
		super(maxDecodeReceivedLength);
	}
}
