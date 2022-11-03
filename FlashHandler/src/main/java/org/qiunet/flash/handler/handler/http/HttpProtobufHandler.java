package org.qiunet.flash.handler.handler.http;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.google.protobuf.CodedInputStream;
import io.micrometer.core.instrument.Timer;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.handler.BaseHandler;
import org.qiunet.function.prometheus.RootRegistry;
import org.qiunet.utils.async.LazyLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class HttpProtobufHandler<RequestData extends IChannelData> extends BaseHandler<RequestData> {
	private final LazyLoader<Codec<RequestData>> codec = new LazyLoader<>(() -> ProtobufDataManager.getCodec(getRequestClass()));
	/**
	 * 计时器
	 */
	private final LazyLoader<Timer> timerRecorder = new LazyLoader<>(() -> {
		return Timer.builder("request.handler.time.counter").tag("protocol", String.valueOf(getProtocolID())).register(RootRegistry.instance.registry());
	});
	/**
	 * 记录耗时
	 * @param useTime
	 */
	@Override
	public void recordUseTime(long useTime) {
		timerRecorder.get().record(useTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public RequestData parseRequestData(ByteBuffer buffer) {
		try {
			return codec.get().readFrom(CodedInputStream.newInstance(buffer));
		} catch (IOException e) {
			logger.error("Request data ["+this.getRequestClass().getName()+"] Protobuf decode exception", e);
		}
		return null;
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
