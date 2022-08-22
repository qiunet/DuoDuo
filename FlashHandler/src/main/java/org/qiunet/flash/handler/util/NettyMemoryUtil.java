package org.qiunet.flash.handler.util;

import io.netty.util.internal.PlatformDependent;
import org.qiunet.utils.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

/***
 * Netty 的内存计数工具
 *
 * @author qiunet
 * 2022/8/23 14:53
 */
public class NettyMemoryUtil {
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
