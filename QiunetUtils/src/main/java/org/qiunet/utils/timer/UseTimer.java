package org.qiunet.utils.timer;

import org.apache.commons.lang.time.StopWatch;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/***
 *
 * @author qiunet
 * 2020-02-08 22:02
 **/
public class UseTimer {

	private static final Logger logger = LoggerType.DUODUO.getLogger();
	/**
	 * 用时表名称
	 */
	private String name;
	/**
	 * 警告时间 毫秒
	 */
	private long warnUseTime;
	/**
	 *
	 */
	private StopWatch stopWatch;
	/**
	 * 没有告警的消息是否打印
	 */
	private boolean printNormalMsg;

	public UseTimer(String name, long warnUseTime) {
		this(name,warnUseTime, true);
	}

	public UseTimer(String name, long warnUseTime, boolean printNormalMsg) {
		this.name = name;
		this.warnUseTime = warnUseTime;
		this.stopWatch = new StopWatch();
		this.printNormalMsg = printNormalMsg;
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
		long useTime = countUseTime();
		if (useTime > warnUseTime) {
			logger.error("{} use {} ms", name, useTime);
		}else if (printNormalMsg){
			logger.info("{} use {} ms", name, useTime);
		}
		return useTime;
	}

	@Override
	public String toString() {
		return name + " current use [" + this.stopWatch.getTime() + "]ms";
	}
}
