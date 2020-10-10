package org.qiunet.flash.handler.context.request.data.pb;

import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 * requestData and responseData 的父类接口.
 *
 * @author qiunet
 * 2020-09-21 16:07
 */
public interface IpbChannelData {
	/**
	 * 转换为byte[]
	 * @return
	 */
	default <T extends IpbChannelData> byte [] toByteArray(){
		return ProtobufDataManager.encode((Class<T>)this.getClass(), (T)this);
	}

	/**
	 * 构造一个IResponseMessage
	 * @return
	 */
	default DefaultProtobufMessage buildResponseMessage(){
		return new DefaultProtobufMessage(getProtocolId(), this);
	}

	/**
	 * 得到protocolId
	 * @return
	 */
	default int getProtocolId() {
		return RequestHandlerMapping.getInstance().getProtocolId(this.getClass());
	}
}
