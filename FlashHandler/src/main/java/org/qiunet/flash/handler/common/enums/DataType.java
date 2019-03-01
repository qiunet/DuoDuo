package org.qiunet.flash.handler.common.enums;

import io.netty.util.CharsetUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 传输使用的数据类型
 * Created by qiunet.
 * 17/11/21
 */
public enum DataType {
	/***
	 * 字符串
	 */
	STRING {
		@Override
		public <T> T parseBytes(byte[] bytes, Object... args) {
			return (T) new String(bytes, CharsetUtil.UTF_8);
		}
	},
	/**
	 * protobuf
	 */
	PROTOBUF {
		private ConcurrentHashMap<Class<?>, Method> class2Method = new ConcurrentHashMap<>(256);
		@Override
		public <T> T parseBytes(byte[] bytes, Object... args) {
			Method method = class2Method.get(args[0]);
			if (method == null) {
				try {
					method = ((Class)args[0]).getMethod("parseFrom", byte[].class);
					class2Method.putIfAbsent((Class<?>) args[0], method);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
			try {
				return (T) method.invoke(null, bytes);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}
	},
	;

	public abstract <T> T parseBytes(byte [] bytes, Object ... args);
}
