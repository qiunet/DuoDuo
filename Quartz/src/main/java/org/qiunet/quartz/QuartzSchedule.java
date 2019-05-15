package org.qiunet.quartz;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.DelayTask;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class QuartzSchedule {
	private Logger logger = LoggerType.DUODUO.getLogger();

	private volatile static QuartzSchedule instance;
	private List<JobFacade> jobs = new ArrayList<>(4);
	private QuartzSchedule() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static QuartzSchedule getInstance() {
		if (instance == null) {
			synchronized (QuartzSchedule.class) {
				if (instance == null)
				{
					new QuartzSchedule();
				}
			}
		}
		return instance;
	}
	/***
	 * 添加一个job到线程调度表
	 * @param job
	 */
	public void addJob(IJob job) {
		this.jobs.add(new JobFacade(job));
	}

	private class JobFacade implements DelayTask<Boolean> {
		private IJob job;

		private Date fireTime;

		private Future<Boolean> future;

		private CronExpression expression;

		public JobFacade(IJob job) {
			this.job = job;
			this.fireTime = new Date();
			try {
				this.expression = new CronExpression(job.cronExpression());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.doNextJob();
		}

		void doNextJob() {
			Date nextDt = expression.getTimeAfter(this.fireTime);
			this.future = TimerManager.getInstance().scheduleWithTimeMillis(this, nextDt.getTime());
		}

		@Override
		public Boolean call() throws Exception {
			this.fireTime = new Date();
			this.doNextJob();
			return this.job.doJob();
		}
	}
}
