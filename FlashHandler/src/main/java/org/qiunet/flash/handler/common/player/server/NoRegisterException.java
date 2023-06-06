package org.qiunet.flash.handler.common.player.server;

/***
 * 没有注册 抛出异常
 * @author qiunet
 * 2023/5/15 12:08
 */
public class NoRegisterException extends Exception {

	public NoRegisterException() {
	}

	public NoRegisterException(String s) {
		super(s);
	}

	public NoRegisterException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoRegisterException(Throwable cause) {
		super(cause);
	}
}
