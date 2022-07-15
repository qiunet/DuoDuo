package org.qiunet.flash.handler.common;

import org.qiunet.utils.string.StringUtil;

/***
 *
 *
 * @author qiunet
 * 2020-09-28 15:57
 */
public class CommMessageHandler extends MessageHandler<CommMessageHandler> {

	private final String msgExecuteIndex;

	public CommMessageHandler() {
		this.msgExecuteIndex = StringUtil.randomString(10);
	}

	@Override
	public String msgExecuteIndex() {
		return msgExecuteIndex;
	}
}

