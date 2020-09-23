package org.qiunet.flash.handler.handler.tcp;

import com.baidu.bjf.remoting.protobuf.Codec;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.io.IOException;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class TcpProtobufHandler<P extends IPlayerActor, RequestData extends IpbRequestData> extends BaseTcpHandler<P, RequestData> {

	private Codec<RequestData> codec;

	@Override
	public RequestData parseRequestData(byte[] bytes){
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
