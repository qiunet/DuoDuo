package org.qiunet.flash.handler.netty.coder;

/***
 *
 * @author qiunet
 * 2022/4/25 15:00
 */
public class KcpSocketDecoder extends WebSocketDecoder {

	public KcpSocketDecoder(int maxReceivedLength, boolean encryption) {
		super(maxReceivedLength, encryption);
	}
}
