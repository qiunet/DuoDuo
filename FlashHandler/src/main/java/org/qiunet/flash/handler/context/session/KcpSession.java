package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.utils.exceptions.CustomException;

/***
 *
 * @author qiunet
 * 2022/4/26 15:46
 */
public class KcpSession extends BaseSession {

	public KcpSession(Channel channel) {
		this.setChannel(channel);
	}

	@Override
	public void bindKcpSession(KcpSession kcpSession) {
		throw new CustomException("Not Support!");
	}

	@Override
	public IDSessionFuture sendKcpMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, true);
	}
}
