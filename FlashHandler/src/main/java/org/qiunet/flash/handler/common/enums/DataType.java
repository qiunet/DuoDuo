package org.qiunet.flash.handler.common.enums;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import io.netty.util.CharsetUtil;

import java.lang.reflect.Field;
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
		private ConcurrentHashMap<Class<?>, Parser> class2Parse = new ConcurrentHashMap<>(256);
		@Override
		public <T> T parseBytes(byte[] bytes, Object... args) {
			Parser<T> parser = class2Parse.get(args[0]);
			if (parser == null) {
				try {
					Field field = ((Class)args[0]).getDeclaredField("PARSER");
					field.setAccessible(true);
					parser = (Parser) field.get(null);
					class2Parse.putIfAbsent((Class<?>) args[0], parser);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			try {
				return parser.parseFrom(bytes);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			return null;
		}
	},
	;

	public abstract <T> T parseBytes(byte [] bytes, Object ... args);
}
