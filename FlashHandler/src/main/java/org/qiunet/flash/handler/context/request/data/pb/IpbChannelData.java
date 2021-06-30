package org.qiunet.flash.handler.context.request.data.pb;

import org.qiunet.flash.handler.context.request.data.IDataToString;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 * requestData and responseData 的父类接口.
 *
 * @author qiunet
 * 2020-09-21 16:07
 */
public interface IpbChannelData extends IDataToString {
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
