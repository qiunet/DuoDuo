package org.qiunet.flash.handler.netty.server.channel;

import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.qiunet.flash.handler.context.response.push.IMessage;

/**
 * 方便广播使用的组
 * Created by qiunet.
 * 17/11/30
 */
public abstract class ServerChannelGroup<Msg> extends DefaultChannelGroup {
	/***
	 * 构造函数
	 * @param name
	 */
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
		return super.writeAndFlush(buildMessage(protocolId, msg).encode(), matcher, false);
	}
	/***
	 * 群发一个webSocket msg
	 * @param protocolId 协议id
	 * @param msg 消息
	 * @return
	 */
	public ChannelGroupFuture wsBroadcast(int protocolId, Msg msg) {
		return this.wsBroadcast(protocolId, msg, ChannelMatchers.all());
	}

	/***
	 * 群发一个webSocket msg
	 * @param protocolId 协议id
	 * @param msg 消息
	 * @param matcher 匹配选择对象
	 * @return
	 */
	public ChannelGroupFuture wsBroadcast(int protocolId, Msg msg, ChannelMatcher matcher) {
		return super.writeAndFlush(new BinaryWebSocketFrame(buildMessage(protocolId, msg).encode().encodeToByteBuf()), matcher);
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
	protected abstract IMessage buildMessage(int protocolId, Msg msg);
}
