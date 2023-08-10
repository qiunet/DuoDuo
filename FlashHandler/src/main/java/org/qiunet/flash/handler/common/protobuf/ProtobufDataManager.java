package org.qiunet.flash.handler.common.protobuf;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import org.qiunet.cross.actor.auth.CrossPlayerAuthRequest;
import org.qiunet.cross.event.CrossEventRequest;
import org.qiunet.cross.rpc.RouteRpcReq;
import org.qiunet.cross.rpc.RouteRpcRsp;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ClientPingRequest;
import org.qiunet.flash.handler.netty.server.config.adapter.message.ServerPongResponse;
import org.qiunet.flash.handler.netty.server.message.ConnectionReq;
import org.qiunet.flash.handler.netty.server.message.ConnectionRsp;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerStartupEvent;

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

	/**
	 * 预热部分协议
	 * @param event
	 */
	@EventListener
	private void startup(ServerStartupEvent event) {
		ProtobufProxy.create(CrossPlayerAuthRequest.class);
		ProtobufProxy.create(CrossEventRequest.class);
		ProtobufProxy.create(ServerPongResponse.class);
		ProtobufProxy.create(ClientPingRequest.class);
		ProtobufProxy.create(RouteRpcReq.class);
		ProtobufProxy.create(RouteRpcRsp.class);
		ProtobufProxy.create(ConnectionReq.class);
		ProtobufProxy.create(ConnectionRsp.class);
	}
}
