package org.qiunet.quartz;

import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.listener.event.data.ServerShutdownEvent;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.math.MathUtil;
import org.qiunet.utils.scanner.event.ScannerOverEvent;
import org.qiunet.utils.timer.IDelayTask;
import org.qiunet.utils.timer.TimerManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CancellationException;

public enum QuartzSchedule {
	instance;

	private final List<JobFacade> jobs = new ArrayList<>(4);

	public static QuartzSchedule getInstance() {
		return instance;
	}
	/***
	 * 添加一个job到线程调度表
	 * @param job 添加的job
	 */
	public DFuture<Boolean> addJob(IJob job) {
		JobFacade jobFacade = new JobFacade(job);
		this.jobs.add(jobFacade);
		return jobFacade.getFuture();
	}
	@EventListener(EventHandlerWeightType.LOWEST)
	private void scannerOver(ScannerOverEvent eventData) {
		this.jobs.forEach(JobFacade::runJob);
	}

	@EventListener
	private void shutdown(ServerShutdownEvent eventData) {
		this.jobs.forEach(job -> {
			job.canceled = true;
			job.future.cancel(false);
		});
	}

	private static  class JobFacade implements IDelayTask<Boolean> {
		/**
		 * 已经停止job
		 */
		private boolean canceled;
		private final IJob job;

		private Date fireTime;

		private DFuture<Boolean> future;

		private CronExpression expression;

		 JobFacade(IJob job) {
			this.job = job;
		 }

		void runJob() {
			this.fireTime = new Date();
			try {
				this.expression = new CronExpression(job.cronExpression());
				this.expression.setTimeZone(TimeZone.getTimeZone(DateUtil.getDefaultZoneId()));
			} catch (ParseException e) {
				LoggerType.DUODUO.error("", e);
			}
			this.doNextJob();
		}

		void doNextJob() {
			if (canceled) {
				return;
			}

			Date nextDt = expression.getTimeAfter(this.fireTime);
			if (nextDt != null) {
				long nextDtTime = nextDt.getTime();
				if (job.randRangeMillis() > 0) {
					nextDtTime += MathUtil.random(job.randRangeMillis());
				}
				this.future = TimerManager.instance.scheduleWithTimeMillis(this, nextDtTime);
				this.future.whenComplete((res, e) ->{
					if (! (e instanceof CancellationException)) {
						this.doNextJob();
					}
				});
			}
		}

		 DFuture<Boolean> getFuture() {
			return future;
		}

		@Override
		public Boolean call() throws Exception {
			 if (canceled) {
				 return false;
			 }
			this.fireTime = new Date();
			return this.job.doJob();
		}
	}
}
