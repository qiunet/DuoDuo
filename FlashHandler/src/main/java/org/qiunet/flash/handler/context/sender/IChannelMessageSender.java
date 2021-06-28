package org.qiunet.flash.handler.context.sender;

import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;

/***
 * 所有发送响应的都实现该接口
 * @author qiunet
 * 2020/3/1 20:53
 **/
public interface IChannelMessageSender extends ISessionMessageSender {
	/***
	 * 得到session
	 */
	DSession getSession();

	@Override
	default IDSessionFuture sendMessage(IChannelMessage<?> message) {
		return getSession().sendMessage(message);
	}

	@Override
	default IDSessionFuture sendMessage(IChannelMessage<?> message, boolean flush){
		return getSession().sendMessage(message, flush);
	}
}
