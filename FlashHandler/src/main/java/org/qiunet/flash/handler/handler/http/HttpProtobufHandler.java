package org.qiunet.flash.handler.handler.http;

import io.micrometer.core.instrument.Timer;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.handler.BaseHandler;
import org.qiunet.function.prometheus.RootRegistry;
import org.qiunet.utils.async.LazyLoader;

import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class HttpProtobufHandler<RequestData extends IChannelData> extends BaseHandler<RequestData> {
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
	public RequestData parseRequestData(MessageContent content) {
		return content.decodeProtobuf(getRequestClass());
	}

	@Override
	public DataType getDataType() {
		return DataType.PROTOBUF;
	}
}
