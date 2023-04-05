package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.pool.ChannelPool;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.DefaultAttributeMap;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/***
 * 有共享Channel 的session
 * 发送消息使用池里的Channel
 * @author qiunet
 * 2023/3/27 14:22
 */
class BaseShareChannelSession extends BaseSession {
	/**
	 * 负责保存session的数据
	 */
	private final DefaultAttributeMap attributeMap = new DefaultAttributeMap();
	/**
	 * 池
	 */
	private final ChannelPool channelPool;

	private final Channel channel;

	public BaseShareChannelSession(ChannelPool channelPool) {
		try {
			this.channel = channelPool.acquire().get(3, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}
		this.channelPool = channelPool;
	}

	@Override
	protected void closeChannel(CloseCause cause) {
		// do nothing
		channelPool.release(channel);
	}

	@Override
	public boolean isActive() {
		return channel.isActive();
	}

	@Override
	public void flush() {
		channel.flush();
	}

	@Override
	public String getIp() {
		return "share-channel";
	}

	@Override
	public <T> Attribute<T> attr(AttributeKey<T> key) {
		return attributeMap.attr(key);
	}

	@Override
	public <T> boolean hasAttr(AttributeKey<T> key) {
		return attributeMap.hasAttr(key);
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		// 都是内网. 3秒足够. 这个只有第一次会有延迟, 之后没有.
		return doSendMessage(channel, message, flush);
	}
}
