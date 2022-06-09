package org.qiunet.flash.handler.context.request.data;

import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;

import java.nio.ByteBuffer;

/***
 * requestData and responseData 的父类接口.
 *
 * @author qiunet
 * 2020-09-21 16:07
 */
public interface IChannelData {
	/**
	 * 转换为byte[]
	 * @return
	 */
	default ByteBuffer toByteBuffer(){
		return ProtobufDataManager.encode(this, true);
	}
	/**
	 * 构造一个IResponseMessage
	 * @return
	 */
	default DefaultProtobufMessage buildChannelMessage(){
		return new DefaultProtobufMessage(protocolId(), this);
	}

	/**
	 * 得到protocolId
	 * @return
	 */
	default int protocolId() {
		return ChannelDataMapping.protocolId(getClass());
	}
}
