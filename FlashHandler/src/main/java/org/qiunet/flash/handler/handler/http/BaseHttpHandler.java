package org.qiunet.flash.handler.handler.http;

import io.micrometer.core.instrument.Timer;
import org.qiunet.flash.handler.common.annotation.UriPathHandler;
import org.qiunet.flash.handler.handler.BaseHandler;
import org.qiunet.function.prometheus.RootRegistry;
import org.qiunet.utils.async.LazyLoader;

import java.util.concurrent.TimeUnit;

/***
 *
 * @author qiunet
 * 2022/11/4 21:00
 */
abstract class BaseHttpHandler<RequestData> extends BaseHandler<RequestData> implements IHttpHandler<RequestData> {
	/**
	 * 计时器
	 */
	private final LazyLoader<Timer> timerRecorder = new LazyLoader<>(() -> {
		String protocolName = "undefine";
		if (getClass().isAnnotationPresent(UriPathHandler.class)) {
			protocolName = getClass().getAnnotation(UriPathHandler.class).value();
		}
		return Timer.builder("request.handler.time.counter").tag("protocol", protocolName).register(RootRegistry.instance.registry());
	});
	/**
	 * 记录耗时
	 * @param useTime
	 */
	public void recordUseTime(long useTime) {
		timerRecorder.get().record(useTime, TimeUnit.MILLISECONDS);
	}
}
