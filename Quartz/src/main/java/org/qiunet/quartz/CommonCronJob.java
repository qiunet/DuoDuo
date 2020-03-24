package org.qiunet.quartz;

import java.lang.reflect.Method;

/****
 * 通过注解调用的一个通用cron调度类
 */
 class CommonCronJob extends BaseJob {

	private String cronExpression;
	private Method workMethod;
	private Object caller;

	 CommonCronJob(String cronExpression, int warnExecMillis,
						 Method workMethod, Object caller) {
	 	super(workMethod, warnExecMillis);
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
			logger.error("Call method ["+jobName+"] with Object ["+caller.getClass().getName()+"] error: ", e);
			return false;
		}
		return true;
	}

	@Override
	public String cronExpression() {
		return cronExpression;
	}
}
