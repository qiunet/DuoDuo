package org.qiunet.flash.handler.common.enums;

/**
 * handler的类型. 区分使用
 * @author qiunet
 *         Created on 17/3/7 17:22.
 */
public enum HandlerType {
	/**
	 * 包括http  https
	 */
	HTTP,
	/**
	 * tcp
	 *  但是udp可以使用该类型的context和handler
	 */
	TCP,
	/**
	 * webSocket
	 */
	WEB_SOCKET,
	/***
	 * 可靠的 UDP
	 */
	UDP,
	;
}
