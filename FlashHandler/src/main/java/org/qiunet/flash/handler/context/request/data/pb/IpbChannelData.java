package org.qiunet.flash.handler.context.request.data.pb;

import org.qiunet.flash.handler.common.protobuf.IProtobufClass;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;

/***
 * requestData and responseData 的父类接口.
 *
 * @author qiunet
 * 2020-09-21 16:07
 */
public interface IpbChannelData extends IProtobufClass {
	/**
	 * 转换为byte[]
	 * @return
	 */
	default byte [] toByteArray(){
		return ProtobufDataManager.encode(this);
	}

	/**
	 * 构造一个IResponseMessage
	 * @return
	 */
	default DefaultProtobufMessage buildResponseMessage(){
		return new DefaultProtobufMessage(protocolId(), this);
	}

	/**
	 * 得到protocolId
	 * @return
	 */
	default int protocolId() {
		return PbChannelDataMapping.protocolId(getClass());
	}
}
