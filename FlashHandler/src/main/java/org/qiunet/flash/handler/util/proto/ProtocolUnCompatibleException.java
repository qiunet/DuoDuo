package org.qiunet.flash.handler.util.proto;

/***
 * 协议不兼容之前版本.
 *
 * @author qiunet
 * 2022/11/11 17:17
 */
public class ProtocolUnCompatibleException extends RuntimeException{
	public ProtocolUnCompatibleException(String message) {
		super(message);
	}
}
