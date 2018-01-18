package org.qiunet.flash.handler.netty.server.http.channel;

import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 方便广播使用的组
 * Created by qiunet.
 * 17/11/30
 */
public class ServerChannelGroup<Msg> extends DefaultChannelGroup {

	public ServerChannelGroup(String name) {
		super(name, GlobalEventExecutor.INSTANCE);
	}
	public ChannelGroupFuture broadcast(Msg msg) {
		return this.writeAndFlush(msg);
	}

	public ChannelGroupFuture broadcast(Msg msg, ChannelMatcher matcher) {
		return this.writeAndFlush(msg, matcher);
	}
}
