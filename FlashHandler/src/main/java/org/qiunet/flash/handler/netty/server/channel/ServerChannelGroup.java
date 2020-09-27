package org.qiunet.flash.handler.netty.server.channel;

import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * 方便广播使用的组
 * Created by qiunet.
 * 17/11/30
 */
public abstract class ServerChannelGroup extends DefaultChannelGroup {
	protected Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/***
	 * 构造函数
	 * @param name
	 */
	public ServerChannelGroup(String name) {
		super(name, GlobalEventExecutor.INSTANCE);
	}

	/***
	 * 对所有人群发一个消息
	 * @param message
	 * @return
	 */
	public ChannelGroupFuture broadcast(IResponseMessage message) {
		return this.broadcast(message, ChannelMatchers.all());
	}
	/***
	 * 群发一个msg
	 * @param message 消息
	 * @param matcher 匹配选择对象
	 * @return
	 */
	public ChannelGroupFuture broadcast(IResponseMessage message, ChannelMatcher matcher) {
		return super.writeAndFlush(message.encode(), matcher, false);
	}

	@Override
	public ChannelGroupFuture writeAndFlush(Object message) {
		throw new CustomException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture write(Object message) {
		throw new CustomException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture write(Object message, ChannelMatcher matcher) {
		throw new CustomException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture write(Object message, ChannelMatcher matcher, boolean voidPromise) {
		throw new CustomException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher) {
		throw new CustomException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher, boolean voidPromise) {
		throw new CustomException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture flushAndWrite(Object message) {
		throw new CustomException("Can called this method , cause message not encode!");
	}

	@Override
	public ChannelGroupFuture flushAndWrite(Object message, ChannelMatcher matcher) {
		throw new CustomException("Can called this method , cause message not encode!");
	}
}
