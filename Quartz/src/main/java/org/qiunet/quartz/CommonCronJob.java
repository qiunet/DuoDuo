package org.qiunet.quartz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/****
 * 通过注解调用的一个通用cron调度类
 */
 class CommonCronJob extends BaseJob {

	private final String cronExpression;
	private final Method workMethod;
	private final Object caller;
	 CommonCronJob(String cronExpression, int warnExecMillis, int randRangeMillis,
						 Method workMethod, Object caller) {
	 	super(workMethod, warnExecMillis, randRangeMillis);
		this.cronExpression = cronExpression;
		this.workMethod = workMethod;
		this.caller = caller;

		this.workMethod.setAccessible(true);
	}

	@Override
	protected Boolean doWork() throws Exception {
		try {
			workMethod.invoke(caller);
		}catch (Exception e) {
			Throwable throwable = e instanceof InvocationTargetException ? ((InvocationTargetException) e).getTargetException() : e;
			logger.error("Call method ["+jobName+"] with Object ["+caller.getClass().getName()+"] error: ", throwable);
			return false;
		}
		return true;
	}

	@Override
	public String cronExpression() {
		return cronExpression;
	}
}
