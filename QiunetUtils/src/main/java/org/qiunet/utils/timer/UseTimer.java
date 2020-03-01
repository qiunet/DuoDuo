package org.qiunet.utils.timer;

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
	 * 开始时间
	 */
	private long startTime;
	/**
	 * 警告时间 毫秒
	 */
	private long warnUseTime;

	public UseTimer(String name, long warnUseTime) {
		this.name = name;
		this.warnUseTime = warnUseTime;
		this.start();
	}

	public void start(){
		this.startTime = System.currentTimeMillis();
	}


	private long countUseTime(){
		return System.currentTimeMillis() - startTime;
	}

	public long printUseTime(){
		long useTime = countUseTime();
		if (useTime > warnUseTime) {
			logger.warn("{} use {} ms", name, useTime);
		}else {
			logger.info("{} use {} ms", name, useTime);
		}
		return useTime;
	}
}
