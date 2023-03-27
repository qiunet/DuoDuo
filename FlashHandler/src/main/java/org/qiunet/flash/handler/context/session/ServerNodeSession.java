package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.header.NodeProtocolHeader;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.util.ChannelUtil;

/***
 *
 * @author qiunet
 * 2023/4/2 09:33
 */
public class ServerNodeSession extends DSession {
	/**
	 * 类型
	 */
	private final NodeSessionType type;
	/**
	 * session 关闭 是否已经通知远端
	 */
	private boolean noticedRemote;


	private final long id;

	public ServerNodeSession(NodeSessionType type, Channel channel, long id) {
		super(channel);
		ChannelUtil.bindSession(this, channel);
		this.type = type;
		this.id = id;
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		type.handMessage(message.add(NodeProtocolHeader.ID_KEY, this.id));
		return super.sendMessage(message, flush);
	}
	/**
	 * 设置已经通知过远端.
	 */
	public void setNoticedRemote() {
		this.noticedRemote = true;
	}

	public boolean isNoticedRemote() {
		return noticedRemote;
	}

	public long getId() {
		return id;
	}
}
