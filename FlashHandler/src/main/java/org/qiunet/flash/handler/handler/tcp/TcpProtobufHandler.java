package org.qiunet.flash.handler.handler.tcp;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.google.protobuf.CodedInputStream;
import io.netty.buffer.ByteBuf;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.handler.BaseHandler;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.protobuf.ProtobufDataManager;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class TcpProtobufHandler<P extends IMessageActor, RequestData extends IpbRequestData> extends BaseHandler<RequestData> implements ITcpHandler<P, RequestData> {
	private LazyLoader<Codec<RequestData>> codec = new LazyLoader<>(() -> ProtobufDataManager.getCodec(getRequestClass()));

	@Override
	public HandlerType getHandlerType() {
		return HandlerType.TCP;
	}

	@Override
	public RequestData parseRequestData(ByteBuffer buffer){
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
