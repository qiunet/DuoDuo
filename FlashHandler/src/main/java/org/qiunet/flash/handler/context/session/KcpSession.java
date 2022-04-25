package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/***
 *
 * @author qiunet
 * 2022/4/26 15:46
 */
public class KcpSession extends BaseSession {

	public KcpSession(Channel channel) {
		channel.attr(ServerConstants.SESSION_KEY).set(this);
		this.setChannel(channel);
	}

	@Override
	public IDSessionFuture sendKcpMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, true);
	}
}
