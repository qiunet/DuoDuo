package org.qiunet.flash.handler.netty.coder;

/***
 *
 * @author qiunet
 * 2022/4/25 15:00
 */
public class KcpSocketClientDecoder extends WebSocketClientDecoder {

	public KcpSocketClientDecoder(int maxReceivedLength, boolean encryption) {
		super(maxReceivedLength, encryption);
	}
}
