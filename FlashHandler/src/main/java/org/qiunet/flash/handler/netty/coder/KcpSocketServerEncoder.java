package org.qiunet.flash.handler.netty.coder;

/***
 *
 *
 * @author qiunet
 * 2022/4/25 15:00
 */
public class KcpSocketServerEncoder extends TcpSocketServerEncoder {

	public KcpSocketServerEncoder(int maxDecodeReceivedLength) {
		super(maxDecodeReceivedLength);
	}
}
