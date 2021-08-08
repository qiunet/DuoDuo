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
		/**跨服服务 直接发给玩家的数据. netty直接转发即可 **/
		int CROSS_2_PLAYER_MSG = 1;
		/**玩家发送的请求 .转发到Cross*/
		int PLAYER_2_CROSS_TRANSMIT_REQ = 2;
		/**服务没有开启*/
		int SERVER_NOT_OPEN_RESP = 3;
		/** 状态消息提示 */
		int ERROR_STATUS_TIPS_RESP = 4;

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
		/** server Node 鉴权 */
		int SERVER_NODE_AUTH = 603;

		int SERVER_NODE_AUTH_RESP = 604;
		/** 跨服玩家鉴权. */
		int CROSS_PLAYER_AUTH = 605;

		/** gm 首页请求 */
		int GM_COMMAND_INDEX_REQ = 606;
		/** gm 首页响应 */
		int GM_COMMAND_LIST_RESP = 607;

		/** gm 调用响应 */
		int GM_COMMAND_REQ = 608;
		/** gm 调用响应 */
		int GM_COMMAND_RESP = 609;
	}
}
