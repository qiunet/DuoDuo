package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.enums.DataType;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class TcpProtobufHandler<RequestData> extends BaseTcpHandler<RequestData> {

	@Override
	public RequestData parseRequestData(byte[] bytes){
		 return getDataType().parseBytes(bytes, getRequestClass());
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
