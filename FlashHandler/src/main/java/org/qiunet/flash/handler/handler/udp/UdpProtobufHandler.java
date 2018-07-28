package org.qiunet.flash.handler.handler.udp;

import com.google.protobuf.GeneratedMessageV3;
import org.qiunet.flash.handler.common.enums.DataType;

import java.lang.reflect.Method;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/28 11:16
 **/
public abstract class UdpProtobufHandler<RequestData extends GeneratedMessageV3> extends BaseUdpHandler<RequestData> {
	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}

	private Method method;
	@Override
	public RequestData parseRequestData(byte[] bytes) {
		try {
			if (method == null ) method = getRequestClass().getMethod("parseFrom", byte[].class);

			return (RequestData) method.invoke(null, bytes);
		} catch (Exception e) {
			logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
		}
		return null;
	}
}
