package org.qiunet.flash.handler.common.id;

/***
 *
 *  * 协议id
 *  *
 *  * 0 - 999 为系统自留
 *  *
 *  * 请求id       = 模块ID * 1000 + 自增
 *  * 响应ID       = (模块ID * 1000 + 请求ID) * 1000 + 自增
 *  * 无请求的响应ID = 模块ID * 1000 * 1000 + 自增
 *  *
 *  * 例如: 模块ID: 1
 *  * 请求ID范围: 1001 ~ 1099
 *  * 响应ID范围: 请求ID+000 ~ 请求ID+999
 *  * 无请求响应ID: 1000000 ~ 1000999
 *
 *  @author qiunet
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
		/**玩家登出*/
		int PLAYER_LOGOUT_PUSH = 5;

		/**玩家需要重新登录*/
		int PLAYER_RE_LOGIN_PUSH = 6;
		/**跨服玩家需要退出当前服务器*/
		int CROSS_PLAYER_NEED_LOGOUT_PUSH = 7;

		/** 404 */
		int HANDLER_NOT_FIND = 404;
		/** 500 **/
		int SERVER_EXCEPTION = 500;

		/** ping pong 信息 */
		int CLIENT_PING = 700;
		int SERVER_PONG = 701;

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
