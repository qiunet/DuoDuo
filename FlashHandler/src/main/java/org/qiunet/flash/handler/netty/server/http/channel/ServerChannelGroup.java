package org.qiunet.flash.handler.netty.server.http.channel;

import io.netty.channel.Channel;
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
public class ServerChannelGroup {
	private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("ServerChannelGroup", GlobalEventExecutor.INSTANCE);

	public static void add(Channel channel) {
		CHANNEL_GROUP.add(channel);
	}

	public static ChannelGroupFuture broadcast(Object msg) {
		return CHANNEL_GROUP.writeAndFlush(msg);
	}

	public static ChannelGroupFuture broadcast(Object msg, ChannelMatcher matcher) {
		return CHANNEL_GROUP.writeAndFlush(msg, matcher);
	}

	public static ChannelGroup flush() {
		return CHANNEL_GROUP.flush();
	}

	public static boolean discard(Channel channel) {
		return CHANNEL_GROUP.remove(channel);
	}

	public static ChannelGroupFuture disconnect() {
		return CHANNEL_GROUP.disconnect();
	}

	public static ChannelGroupFuture disconnect(ChannelMatcher matcher) {
		return CHANNEL_GROUP.disconnect(matcher);
	}

	public static boolean contains(Channel channel) {
		return CHANNEL_GROUP.contains(channel);
	}

	public static int size() {
		return CHANNEL_GROUP.size();
	}
}
