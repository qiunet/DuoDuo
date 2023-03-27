package org.qiunet.flash.handler.context.session;

import org.qiunet.flash.handler.context.response.push.IChannelMessage;

/***
 * NodeSession 类型
 *
 * @author qiunet
 * 2023/3/29 17:26
 */
public enum NodeSessionType {
	CROSS_PLAYER {
		@Override
		public void handMessage(IChannelMessage message) {
			message.asCrossPlayerMsg();
		}
	},
	SERVER_NODE  {
		@Override
		public void handMessage(IChannelMessage message) {
			message.asServerNodeMsg();
		}
	},
	;

	public abstract void handMessage(IChannelMessage message);
}
