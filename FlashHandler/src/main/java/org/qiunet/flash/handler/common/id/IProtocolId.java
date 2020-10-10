package org.qiunet.flash.handler.common.id;

/***
 *
 * @author qiunet
 * 2020-10-10 22:14
 **/
public interface IProtocolId {
	/**
	 * 系统相关
	 */
	interface System {
		/**1 上行协议有错误. 具体读取ErrorResponse 的desc**/
		int PROTOCOL_ERROR = 1;
		/** 404 */
		int HANDLER_NOT_FIND = 404;
		/** 500 **/
		int SERVER_EXCEPTION = 500;
	}
}
