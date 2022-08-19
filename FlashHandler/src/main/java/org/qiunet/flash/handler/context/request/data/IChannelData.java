package org.qiunet.flash.handler.context.request.data;

import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;

/***
 * requestData and responseData 的父类接口.
 *
 * @author qiunet
 * 2020-09-21 16:07
 */
public interface IChannelData {
	/**
	 * 转换为ByteBuf 最终记得要release
	 * @return
	 */
	default ByteBuf toByteBuf() {
		return ProtobufDataManager.encodeToByteBuf(this);
	}
	/**
	 * 转换为byte[]
	 * @return
	 */
	default byte[] toByteArray(){
		return ProtobufDataManager.encodeToByteArray(this);
	}
	/**
	 * 构造一个IResponseMessage
	 * @return
	 */
	default DefaultProtobufMessage buildChannelMessage(){
		return DefaultProtobufMessage.valueOf(protocolId(), this);
	}

	/**
	 * 得到protocolId
	 * @return
	 */
	default int protocolId() {
		return ChannelDataMapping.protocolId(getClass());
	}
}
