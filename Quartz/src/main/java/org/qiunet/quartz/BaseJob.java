package org.qiunet.quartz;

import org.qiunet.utils.thread.ThreadContextData;
import org.qiunet.utils.timer.UseTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by qiunet on 4/12/17.
 */
abstract class BaseJob implements IJob {
	protected final static Logger logger = LoggerFactory.getLogger(BaseJob.class);
	/**
	 * job 的名称
	 */
	protected String jobName;
	/**
	 * 告警的执行时间毫秒
	 */
	protected int warnMillis;

	protected BaseJob(Method method, int warnMillis) {
		this.jobName = method.getDeclaringClass().getName() +
			"." + method.getName();
		this.warnMillis = warnMillis;
	}

	@Override
	public Boolean doJob(){
		UseTimer useTimer = new UseTimer(jobName, warnMillis);
		useTimer.start();
		try {
			 return doWork();
		}catch (Exception e) {
			logger.error("Job  ["+jobName+"] Exception ", e);
		}
		finally {
			ThreadContextData.removeAll();
			useTimer.printUseTime();
		}
		return true;
	}
	/***
	 * 调度
	 * @return
	 */
	protected abstract Boolean doWork() throws Exception;

}
