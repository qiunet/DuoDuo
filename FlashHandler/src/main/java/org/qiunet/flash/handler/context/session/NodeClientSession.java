package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelFuture;
import io.netty.channel.pool.ChannelPool;
import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.context.header.NodeProtocolHeader;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/***
 * 节点间的Session
 * @author qiunet
 * 2023/3/27 14:49
 */
public class NodeClientSession extends BaseShareChannelSession {
	/**
	 * session 关闭 是否已经通知远端
	 */
	private boolean noticedRemote;

	private final NodeSessionType type;

	private final long id;

	public NodeClientSession(NodeSessionType type, ChannelPool channelPool, long id) {
		super(channelPool);
		this.attachObj(ServerConstants.HANDLER_TYPE_KEY, ServerConnType.TCP);
		this.type = type;
		this.id = id;
	}
	/**
	 * 设置已经通知过远端.
	 */
	public void setNoticedRemote() {
		this.noticedRemote = true;
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		type.handMessage(message.add(NodeProtocolHeader.ID_KEY, this.id));
		return super.sendMessage(message, flush);
	}

	public boolean isNoticedRemote() {
		return noticedRemote;
	}

	@Override
	public String aliasId() {
		return this.type+":"+super.aliasId();
	}

	@Override
	public String getIp() {
		return "cross node";
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "NCS[type=" + type + ", id=" + id +  ", aliasId=" + aliasId() +']';
	}
}
