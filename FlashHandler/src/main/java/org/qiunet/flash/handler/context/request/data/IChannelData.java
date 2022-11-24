package org.qiunet.flash.handler.context.request.data;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;

/***
 * requestData and responseData 的父类接口.
 *
 * @author qiunet
 * 2020-09-21 16:07
 */
@ProtobufClass
public abstract class IChannelData {
	/**
	 * 转换为ByteBuf 最终记得要release
	 * @return
	 */
	public ByteBuf toByteBuf() {
		return ProtobufDataManager.encodeToByteBuf(this);
	}
	/**
	 * 转换为byte[]
	 * @return
	 */
	public byte[] toByteArray(){
		return ProtobufDataManager.encodeToByteArray(this);
	}
	/**
	 * 构造一个IResponseMessage
	 * @return
	 */
	public DefaultProtobufMessage buildChannelMessage(){
		return DefaultProtobufMessage.valueOf(protocolId(), this);
	}

	/**
	 * channel data to bytebuf 后.
	 * 会调用该方法. 如果需要回收的东西.
	 * 可以写这个里面
	 */
	public void recycle() {}
	/**
	 * 得到protocolId
	 * @return
	 */
	public int protocolId() {
		return ChannelDataMapping.protocolId(getClass());
	}
}
