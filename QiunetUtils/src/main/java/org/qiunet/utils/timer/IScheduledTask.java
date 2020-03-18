package org.qiunet.utils.timer;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * 异步的时间调度的一个类
 * Created by qiunet.
 * 18/1/26
 */
public interface IScheduledTask extends Runnable {
	 Logger logger = LoggerType.DUODUO.getLogger();

	@Override
	default void run() {
		try {
			run0();
		}catch (Exception e) {
			logger.error("AsyncTimerTask: Exception: ", e);
		}
	}

	/***
	 *  调度执行的方法
	 */
	void run0();
}
