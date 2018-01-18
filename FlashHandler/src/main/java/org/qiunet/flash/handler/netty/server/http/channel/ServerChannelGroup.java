package org.qiunet.flash.handler.netty.server.http.channel;

import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.qiunet.flash.handler.common.message.MessageContent;

/**
 * 方便广播使用的组
 * Created by qiunet.
 * 17/11/30
 */
public abstract class ServerChannelGroup<Msg> extends DefaultChannelGroup {

	public ServerChannelGroup(String name) {
		super(name, GlobalEventExecutor.INSTANCE);
	}

	/***
	 * 对所有人群发一个消息
	 * @param protocolId
	 * @param msg
	 * @return
	 */
	public ChannelGroupFuture broadcast(int protocolId, Msg msg) {
		return this.broadcast(protocolId, msg, ChannelMatchers.all());
	}
	/***
	 * 群发一个msg
	 * @param protocolId 协议id
	 * @param msg 消息
	 * @param matcher 匹配选择对象
	 * @return
	 */
	public ChannelGroupFuture broadcast(int protocolId, Msg msg, ChannelMatcher matcher) {
		MessageContent content = new MessageContent(protocolId, bytes(msg));
		return super.writeAndFlush(content, matcher);
	}

	@Override
	public ChannelGroupFuture writeAndFlush(Object message) {
		throw new RuntimeException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture write(Object message) {
		throw new RuntimeException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture write(Object message, ChannelMatcher matcher) {
		throw new RuntimeException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture write(Object message, ChannelMatcher matcher, boolean voidPromise) {
		throw new RuntimeException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher) {
		throw new RuntimeException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher, boolean voidPromise) {
		throw new RuntimeException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture flushAndWrite(Object message) {
		throw new RuntimeException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture flushAndWrite(Object message, ChannelMatcher matcher) {
		throw new RuntimeException("Can called this method , cause message not encode!");
	}
	/***
	 * 转换成bytes
	 * @param msg
	 * @return
	 */
	protected abstract byte[] bytes(Msg msg);
}
