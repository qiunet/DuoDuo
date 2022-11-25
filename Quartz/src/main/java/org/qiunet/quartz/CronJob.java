package org.qiunet.quartz;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.IDelayTask;

/***
 * 外面定义的 cron
 *
 * @author qiunet
 * 2022/12/1 15:38
 */
public class CronJob implements IJob {
	/**
	 * 需要执行的内容
	 */
	private IDelayTask<Boolean> runnable;
	/**
	 * cron 表达式
	 */
	private String cronExpression;
	/**
	 * 任务名
	 */
	private String jobName;
	private CronJob() {}

	/**
	 * 新增一个 cron
	 * @param cronExpression 表达式
	 * @param runnable true 表示成功 false 失败
	 */
	public static CronJob newCron(String cronExpression, String jobName, IDelayTask<Boolean> runnable){
		CronJob cronJob = new CronJob();
		cronJob.cronExpression = cronExpression;
		cronJob.runnable = runnable;
		cronJob.jobName = jobName;
		QuartzSchedule.getInstance().addJob(cronJob);
		return cronJob;
	}
	@Override
	public Boolean doJob() {
		try {
			return runnable.call();
		}catch (Throwable cause) {
			LoggerType.DUODUO.error("Job  ["+jobName+"] Exception ", cause);
			return false;
		}
	}

	@Override
	public String cronExpression() {
		return cronExpression;
	}
}
