package org.qiunet.utils.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.google.common.base.Preconditions;
import com.google.protobuf.CodedInputStream;
import org.qiunet.utils.exceptions.CustomException;

import java.io.IOException;
import java.nio.ByteBuffer;

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
	 * @param obj
	 * @return
	 */
	public static byte[] encode(Object obj) {
		Preconditions.checkNotNull(obj);
		Class objClass = obj.getClass();
		try {
			return getCodec(objClass).encode(obj);
		} catch (Exception e) {
			throw new CustomException(e, "Class [{}] encode exception", objClass.getName());
		}
	}

	/**
	 * 反序列对象出来.
	 * @param clazz
	 * @param buffer
	 * @param <T>
	 * @return
	 */
	public static <T> T decode(Class<T> clazz, ByteBuffer buffer) {
		try {
			return getCodec(clazz).readFrom(CodedInputStream.newInstance(buffer));
		} catch (IOException e) {
			throw new CustomException(e, "Class ["+clazz.getName()+"] decode data error!");
		}
	}

	public static <T> T decode(Class<T> clazz, byte [] bytes) {
		try {
			return getCodec(clazz).decode(bytes);
		} catch (IOException e) {
			throw new CustomException(e, "Class ["+clazz.getName()+"] decode data error!");
		}
	}
}
