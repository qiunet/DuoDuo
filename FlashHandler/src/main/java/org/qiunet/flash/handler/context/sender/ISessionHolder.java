package org.qiunet.flash.handler.context.sender;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISender;
import org.qiunet.flash.handler.context.session.ISession;

/***
 * 所有发送响应的都实现该接口
 * @author qiunet
 * 2020/3/1 20:53
 **/
public interface ISessionHolder extends ISender {
	/***
	 * 得到session
	 */
	ISession getSession();
	/**
	 * 发送消息
	 * @param message
	 */
	default ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush){
		return this.getSession().sendMessage(message, flush);
	}
}
