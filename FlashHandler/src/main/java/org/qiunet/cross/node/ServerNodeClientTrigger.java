package org.qiunet.cross.node;

import io.netty.channel.Channel;
import org.qiunet.cross.pool.NodeChannelTrigger;
import org.qiunet.flash.handler.context.header.INodeServerHeader;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.util.ChannelUtil;

/***
 * Cross Tcp客户端响应处理
 *
 * @author qiunet
 * 2020-10-23 17:44
 */
public class ServerNodeClientTrigger implements NodeChannelTrigger {

	@Override
	public ISession getNodeSession(Channel channel, INodeServerHeader header) {
		return ChannelUtil.getSession(channel);
	}

	@Override
	public boolean serverNode() {
		return true;
	}
}
