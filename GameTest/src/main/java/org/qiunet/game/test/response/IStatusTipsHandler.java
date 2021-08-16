package org.qiunet.game.test.response;

import org.qiunet.flash.handler.netty.server.param.adapter.message.StatusTipsResponse;

/***
 * 在 actionNode 处理状态
 * 处理的状态id 请使用 {@link StatusTipsHandler} 注解标注出来
 * qiunet
 * 2021/8/8 23:01
 **/
public interface IStatusTipsHandler {
	/**
	 * 处理状态.
	 * @param response
	 */
	void statusHandler(StatusTipsResponse response);
}
