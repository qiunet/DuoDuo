package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;

/***
 *
 * @author qiunet
 * 2023/3/24 20:36
 */
public class ClientSession extends BaseChannelSession {

	public ClientSession(Channel channel) {
		super.setChannel(channel);
	}
}
