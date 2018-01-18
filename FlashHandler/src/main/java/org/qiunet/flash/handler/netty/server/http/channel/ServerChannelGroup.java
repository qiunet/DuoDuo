package org.qiunet.flash.handler.netty.server.http.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 方便广播使用的组
 * Created by qiunet.
 * 17/11/30
 */
public class ServerChannelGroup<Msg> {
	private final ChannelGroup channelGroup;

	public ServerChannelGroup(String name) {
		channelGroup = new DefaultChannelGroup(name, GlobalEventExecutor.INSTANCE);
	}

	public void add(Channel channel) {
		channelGroup.add(channel);
	}

	public ChannelGroupFuture broadcast(Msg msg) {
		return channelGroup.writeAndFlush(msg);
	}

	public ChannelGroupFuture broadcast(Msg msg, ChannelMatcher matcher) {
		return channelGroup.writeAndFlush(msg, matcher);
	}

	public Channel find(ChannelId channelId) {
		return channelGroup.find(channelId);
	}

	public ChannelGroup flush() {
		return channelGroup.flush();
	}

	public boolean remove(Channel channel) {
		return channelGroup.remove(channel);
	}

	public ChannelGroupFuture disconnect() {
		return channelGroup.disconnect();
	}

	public ChannelGroupFuture disconnect(ChannelMatcher matcher) {
		return channelGroup.disconnect(matcher);
	}

	public boolean contains(Channel channel) {
		return channelGroup.contains(channel);
	}

	public int size() {
		return channelGroup.size();
	}
}
