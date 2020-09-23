package org.qiunet.flash.handler.handler.http;

import com.baidu.bjf.remoting.protobuf.Codec;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.io.IOException;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class HttpProtobufHandler<RequestData extends IpbRequestData, ResponseData extends IpbResponseData> extends BaseHttpHandler<RequestData, ResponseData> {

	private Codec<RequestData> codec;

	@Override
	public RequestData parseRequestData(byte[] bytes) {
		if (codec == null) {
			codec = ProtobufDataManager.getCodec(getRequestClass());
		}
		try {
			return codec.decode(bytes);
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
