package org.qiunet.flash.handler.handler.http;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.google.protobuf.CodedInputStream;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.utils.async.LazyLoader;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class HttpProtobufHandler<RequestData extends IChannelData, ResponseData extends IChannelData> extends BaseHttpHandler<RequestData, ResponseData> {
	private final LazyLoader<Codec<RequestData>> codec = new LazyLoader<>(() -> ProtobufDataManager.getCodec(getRequestClass()));

	@Override
	public RequestData parseRequestData(ByteBuffer buffer) {
		try {
			return codec.get().readFrom(CodedInputStream.newInstance(buffer));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
