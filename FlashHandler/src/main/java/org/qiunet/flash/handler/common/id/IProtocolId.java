package org.qiunet.flash.handler.common.id;

/***
 *  * 协议id
 *  *
 *  * 0 - 999 为系统自留
 *  *
 *  * 请求id       = 模块ID * 100 + 1 起自增
 *  * 响应ID       = 模块ID * 100 + 请求ID * 1000 起自增
 *  * 无请求的响应ID = 模块ID * 100 * 1000 起自增
 *  *
 *  * 例如: 模块ID: 10
 *  * 请求ID范围: 1001 - 1099
 *  * 响应ID范围: 1001000 - 1099999
 *  * 无请求响应ID: 1000000 - 1000999
 *
 * @author qiunet
 * 2020-10-10 22:14
 **/
public interface IProtocolId {
	/**
	 * 系统相关
	 * 模块id: 0
	 */
	interface System {
		/** 404 */
		int HANDLER_NOT_FIND = 404;
		/** 500 **/
		int SERVER_EXCEPTION = 500;

		/**跨服事件**/
		int CROSS_EVENT = 600;

		/** 事务请求 **/
		int TRANSACTION_REQ = 601;
		/** 事务响应 **/
		int TRANSACTION_RESP = 602;
	}
}
