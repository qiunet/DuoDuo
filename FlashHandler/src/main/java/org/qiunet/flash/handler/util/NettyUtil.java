package org.qiunet.flash.handler.util;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.PlatformDependent;
import org.qiunet.utils.async.factory.DefaultThreadFactory;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

/***
 * Netty 工具
 *
 * @author qiunet
 * 2022/8/23 14:53
 */
public class NettyUtil {
	/**
	 * 构造一个 EventLoopGroup
	 * @param threadNum 线程数
	 * @param pollName 线程名
	 * @return  EventLoopGroup
	 */
	public static EventLoopGroup newEventLoopGroup(int threadNum, String pollName) {
		if (Epoll.isAvailable()) {
			return new EpollEventLoopGroup(threadNum, new DefaultThreadFactory("epoll-"+pollName));
		}
		return new NioEventLoopGroup(threadNum, new DefaultThreadFactory(pollName));
	}

	/**
	 * 获得 ServerSocketChannel class
	 * @return ServerSocketChannel class
	 */
	public static Class<? extends ServerSocketChannel> serverSocketChannelClass() {
		if (Epoll.isAvailable()) {
			return EpollServerSocketChannel.class;
		}
		return NioServerSocketChannel.class;
	}

	/**
	 * 获得使用的直接内存
	 * @return
	 */
	public static long usedDirectMemory() {
		try {
			Field field = PlatformDependent.class.getDeclaredField("DIRECT_MEMORY_COUNTER");
			ReflectUtil.makeAccessible(field);

			AtomicLong counter = (AtomicLong) field.get(null);
			return counter == null ? -1 : counter.get();
		} catch (Exception e) {
			return Integer.MIN_VALUE;
		}
	}
	/**
	 * 获得最大的直接内存
	 * @return
	 */
	public static long directMemoryLimit() {
		try {
			Field field = PlatformDependent.class.getDeclaredField("DIRECT_MEMORY_LIMIT");
			ReflectUtil.makeAccessible(field);
			return (long) field.get(null);
		} catch (Exception e) {
			return -1;
		}
	}
}
