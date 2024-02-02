package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.DefaultAttributeMap;
import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.player.event.CrossChannelErrorEvent;
import org.qiunet.flash.handler.context.header.NodeProtocolHeader;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/***
 * 使用的共享Channel 连接的Session
 *
 * @author qiunet
 * 2023/3/27 16:03
 */
public class NodeServerSession extends BaseChannelSession {
	/**
	 * 负责保存session的数据
	 */
	private final DefaultAttributeMap attributeMap = new DefaultAttributeMap();
	/**
	 * 类型
	 */
	private final NodeSessionType type;
	/**
	 * session 关闭 是否已经通知远端
	 */
	private boolean noticedRemote;


	private final long id;

	public NodeServerSession(NodeSessionType type, Channel channel, long id) {
		this.setChannel(channel);
		this.type = type;
		this.id = id;
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		type.handMessage(message.add(NodeProtocolHeader.ID_KEY, this.id));
		return super.sendMessage(message, flush);
	}

	@Override
	public void close(CloseCause cause) {
		if (cause == CloseCause.DECODE_ERROR && type == NodeSessionType.CROSS_PLAYER) {
			// 这种情况,只通知Gate Channel下线重连, 不要直接退房.
			CrossPlayerActor actor = (CrossPlayerActor) getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			actor.fireCrossEvent(CrossChannelErrorEvent.valueOf(cause));
			return;
		}

		super.close(cause);
	}

	@Override
	public <T> Attribute<T> attr(AttributeKey<T> key) {
		return attributeMap.attr(key);
	}

	@Override
	public <T> boolean hasAttr(AttributeKey<T> key) {
		return attributeMap.hasAttr(key);
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

	@Override
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public boolean isActive() {
		return !closed.get() && super.isActive();
	}

	@Override
	public String aliasId() {
		return this.type +":"+super.aliasId();
	}

	@Override
	public String getIp() {
		return "cross node";
	}

	@Override
	protected void closeChannel(CloseCause cause) {
		this.flush();
	}

	public long getId() {
		return id;
	}
}
