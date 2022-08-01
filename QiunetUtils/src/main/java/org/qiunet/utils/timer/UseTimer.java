package org.qiunet.utils.timer;

import org.apache.commons.lang3.time.StopWatch;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.function.Supplier;

/***
 *
 * @author qiunet
 * 2020-02-08 22:02
 **/
public class UseTimer {

	private static final Logger logger = LoggerType.DUODUO_USE_TIME.getLogger();
	/**
	 * 用时表名称
	 */
	private final Supplier<String> nameGetter;
	/**
	 * 警告时间 毫秒
	 */
	private final long warnUseTime;
	/**
	 *
	 */
	private final StopWatch stopWatch;

	public UseTimer(String name, long warnUseTime) {
		this(() -> name, warnUseTime);
	}

	public UseTimer(Supplier<String> nameGetter, long warnUseTime) {
		this.nameGetter = nameGetter;
		this.warnUseTime = warnUseTime;
		this.stopWatch = new StopWatch();
		this.start();
	}

	public void reset() {
		this.stopWatch.reset();
	}

	public void start(){
		this.reset();
		this.stopWatch.start();
	}


	private long countUseTime(){
		this.stopWatch.stop();
		return stopWatch.getTime();
	}

	public long printUseTime(){
		return this.printUseTime(null);
	}
	/**
	 *
	 * @param supplement 补充打印的信息
	 * @return
	 */
	public long printUseTime(Supplier<String> supplement){
		long useTime = countUseTime();
		if (useTime > warnUseTime) {
			if (supplement != null) {
				logger.error("{} use {} ms! detail: {}", nameGetter.get(), useTime, supplement.get());
			}else {
				logger.error("{} use {} ms", nameGetter.get(), useTime);
			}
		}
		return useTime;
	}

	@Override
	public String toString() {
		return nameGetter.get() + " current use [" + this.stopWatch.getTime() + "]ms";
	}
}
