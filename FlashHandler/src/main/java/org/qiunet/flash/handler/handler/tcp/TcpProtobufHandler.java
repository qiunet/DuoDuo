package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.handler.tcp.BaseTcpHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class TcpProtobufHandler<RequestData> extends BaseTcpHandler<RequestData> {
	private Method method;
	@Override
	public RequestData parseRequestData(byte[] bytes){
		 {
			try {
				if (method == null ) method = getRequestClass().getMethod("parseFrom", byte[].class);
				return (RequestData) method.invoke(null, bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 return null;
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
