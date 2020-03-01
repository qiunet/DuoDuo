package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;

/**
 * session 的接口,
 * Created by qiunet.
 * 17/10/23
 */
public interface ISession {


	/***
	 * 得到存放map的唯一key
	 * @return
	 */
	long getUid();

	/***
	 *
	 * @return
	 */
	Channel getChannel();

	/***
	 * 对外写消息
	 * @param message
	 */
	ChannelFuture writeMessage(IResponseMessage message);
}
