package org.qiunet.flash.handler.common.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.google.common.base.Preconditions;
import com.google.protobuf.CodedInputStream;
import io.netty.buffer.ByteBuf;
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
	 * @param clazz 获得Codec
	 * @param <T> class泛型
	 * @return Codec实例
	 */
	public static <T> Codec<T> getCodec(Class<T> clazz) {
		return ProtobufProxy.create(clazz);
	}
	/**
	 * 序列化对象成byte数组
	 * @param obj 需要enode的对象
	 * @return 数组
	 */
	public static byte[] encodeToByteArray(Object obj) {
		Preconditions.checkNotNull(obj);
		Class objClass = obj.getClass();
		try {
			return getCodec(objClass).encode(obj);
		} catch (Exception e) {
			throw new CustomException(e, "Class [{}] encode exception", objClass.getName());
		}
	}

	/**
	 * 序列化为 ByteBuf
	 * @param obj 需要encode的对象
	 * @return ByteBuf
	 */
	public static ByteBuf encodeToByteBuf(Object obj) {
		Preconditions.checkNotNull(obj);
		Class objClass = obj.getClass();
		Codec codec = getCodec(objClass);
		CodedOutputStreamThreadCache cache = null;
		ByteBuf byteBuf = null;
		try {
			cache = CodedOutputStreamThreadCache.get();
			codec.writeTo(obj, cache.getCodedOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally {
			if (cache != null) {
				byteBuf = cache.recycle();
			}
		}
		return byteBuf;
	}

	/**
	 * 反序列对象出来.
	 * @param clazz 需要反序列的目标对象
	 * @param buffer 数据buffer
	 * @param <T> 泛型
	 * @return 对象实例
	 */
	public static <T> T decode(Class<T> clazz, ByteBuffer buffer) {
		CodedInputStreamThreadCache in = CodedInputStreamThreadCache.get(buffer);
		try {
			return getCodec(clazz).readFrom(in.getCodedInputStream());
		} catch (IOException e) {
			throw new CustomException(e, "Class ["+clazz.getName()+"] decode data error!");
		}finally {
			in.recycle();
		}
	}
}
