package org.qiunet.quartz;

import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.timer.IDelayTask;
import org.qiunet.utils.timer.TimerManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuartzSchedule {

	private volatile static QuartzSchedule instance;
	private List<JobFacade> jobs = new ArrayList<>(4);
	private QuartzSchedule() {
		if (instance != null) throw new CustomException("Instance Duplication!");
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
	public DFuture<Boolean> addJob(IJob job) {
		JobFacade jobFacade = new JobFacade(job);
		this.jobs.add(jobFacade);
		return jobFacade.getFuture();
	}

	private static  class JobFacade implements IDelayTask<Boolean> {
		private IJob job;

		private Date fireTime;

		private DFuture<Boolean> future;

		private CronExpression expression;

		 JobFacade(IJob job) {
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
			if (nextDt != null) {
				this.future = TimerManager.instance.scheduleWithTimeMillis(this, nextDt.getTime());
				this.future.whenComplete((res, e) -> this.doNextJob());
			}
		}

		 DFuture<Boolean> getFuture() {
			return future;
		}

		@Override
		public Boolean call() throws Exception {
			this.fireTime = new Date();
			return this.job.doJob();
		}
	}
}
