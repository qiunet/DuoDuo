package org.qiunet.quartz;

import java.lang.reflect.Method;

/****
 * 通过注解调用的一个通用cron调度类
 */
public class CommonCronJob extends BaseJob {

	private String cronExpression;
	private boolean logExceInfo;
	private Method workMethod;
	private Object caller;

	public CommonCronJob(String cronExpression, boolean logExceInfo,
						 Method workMethod, Object caller) {
		this.cronExpression = cronExpression;
		this.logExceInfo = logExceInfo;
		this.workMethod = workMethod;
		this.caller = caller;

		this.workMethod.setAccessible(true);
	}

	@Override
	protected Boolean doWork() throws Exception {
		try {
			workMethod.invoke(caller);
		}catch (Exception e) {
			logger.error("Call method ["+workMethod.getName()+"] with Object ["+caller.getClass().getName()+"] error: ", e);
			return false;
		}
		return true;
	}

	@Override
	protected boolean logExecInfo() {
		return logExceInfo;
	}

	@Override
	public String cronExpression() {
		return cronExpression;
	}
}
