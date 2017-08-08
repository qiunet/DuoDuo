package org.qiunet.flash.handler.session;

/**
 * netty的session
 * Created by qiunet.
 * 17/7/24
 */
public interface GameSession<RequestData> {
	/**
	 * 得到session 的id
	 * @return
	 */
	String getId();
	/**
	 * 处理消息往哪扔.
	 * 屏蔽并发使用
	 * @param requestData
	 */
	void handlerMsg(RequestData requestData);
}
