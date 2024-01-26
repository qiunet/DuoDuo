package org.qiunet.flash.handler.common.protobuf;

import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.utils.string.StringUtil;

import java.util.Arrays;

/***
 * protobuf解析异常
 * @author qiunet
 * 2024/1/16 20:58
 ***/
public class ProtoDecodeException extends RuntimeException {

	public ProtoDecodeException(Throwable cause, Class<?> channelDataClz, IProtocolHeader.ProtocolHeader header, byte [] data) {
		super(StringUtil.slf4jFormat("Class[{}] Decode error, header: {}, data: {}", channelDataClz.getName(), header, Arrays.toString(data)), cause);
	}

}
