package org.qiunet.flash.handler.context.request.data.pb;


import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;

/***
 * protobuf的响应接口
 *
 * @author qiunet
 * 2020-09-21 15:17
 */
public interface IpbResponseData extends IpbData {
	/**
	 * 得到protocolId
	 * @return
	 */
	default int getProtocolId() {
		return PbResponseDataMapping.protocolId(getClass());
	}

	/**
	 * 构造一个IResponseMessage
	 * @return
	 */
	default IResponseMessage buildResponseMessage(){
		return new DefaultProtobufMessage(getProtocolId(), this);
	}
}
