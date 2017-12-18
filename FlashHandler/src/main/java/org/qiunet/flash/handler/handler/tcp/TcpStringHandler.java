package org.qiunet.flash.handler.handler.tcp;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.handler.tcp.BaseTcpHandler;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/11/21
 */
public abstract class TcpStringHandler extends BaseTcpHandler<String> {

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String parseRequestData(byte[] bytes) {
		return new String(bytes, CharsetUtil.UTF_8);
	}
}
