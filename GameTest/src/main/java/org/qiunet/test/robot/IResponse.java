package org.qiunet.test.robot;

import org.qiunet.flash.handler.context.header.MessageContent;

/**
 * Created by qiunet.
 * 17/12/4
 */
public interface IResponse {
	/***
	 * 响应数据
	 * @param content
	 */
	void response(MessageContent content);
}
