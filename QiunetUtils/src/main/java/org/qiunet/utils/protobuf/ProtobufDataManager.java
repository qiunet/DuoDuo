package org.qiunet.utils.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import org.qiunet.utils.exceptions.CustomException;

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
	 * 序列化对象成byte数组
	 * @param clazz
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> byte[] encode(Class<T> clazz, T obj) {
		try {
			return getCodec(clazz).encode(obj);
		} catch (Exception e) {
			throw new CustomException(e, "Class [{}] encode exception", clazz.getName());
		}
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
		throw new CustomException("Class ["+clazz.getName()+"] decode data error!");
	}
}
