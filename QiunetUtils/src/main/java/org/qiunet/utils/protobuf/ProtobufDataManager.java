package org.qiunet.utils.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;

import java.io.IOException;

/***
 * 管理protobuf codec的类.
 *
 * @author qiunet
 * 2020-09-22 11:34
 */
public class ProtobufDataManager {
	/**
	 * 获得某个class的codec
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> Codec<T> getCodec(Class<T> clazz) {
		return ProtobufDataContext0.getInstance().codec(clazz);
	}

	/**
	 * 反序列对象出来.
	 * @param clazz
	 * @param bytes
	 * @param <T>
	 * @return
	 */
	public static <T> T decode(Class<T> clazz, byte [] bytes) {
		try {
			return getCodec(clazz).decode(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Class ["+clazz.getName()+"] decode data error!");
	}
}
