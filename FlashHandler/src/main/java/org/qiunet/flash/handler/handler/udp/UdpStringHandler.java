package org.qiunet.flash.handler.handler.udp;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.enums.DataType;

import java.lang.reflect.Method;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/28 11:16
 **/
public abstract class UdpStringHandler extends BaseUdpHandler<String> {
	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String parseRequestData(byte[] bytes) {
		return getDataType().parseBytes(bytes);
	}
}
