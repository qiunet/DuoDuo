package org.qiunet.quartz;

import org.qiunet.utils.asyncQuene.mutiThread.MultiAsyncQueueHandler;
import org.qiunet.utils.hook.ShutdownHookThread;
import org.qiunet.utils.timer.AsyncTimerTask;
import org.qiunet.utils.timer.TimerManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuartzSchedule extends AsyncTimerTask {
	private MultiAsyncQueueHandler workers = new MultiAsyncQueueHandler("QuartzScheduleWorkers", 4, 50, 60, TimeUnit.SECONDS);
	private List<JobFacade> jobs = new ArrayList(4);
	private volatile static QuartzSchedule instance;

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
		if (jobs.isEmpty()) {
			/**5秒后 开始执行调度*/
			TimerManager.getInstance().scheduleAtFixedRate(this, 3000, 1000);

			ShutdownHookThread.getInstance().addShutdownHook( () -> {
				TimerManager.getInstance().shutdown();
				QuartzSchedule.getInstance().shutdown();
			});
		}
		this.jobs.add(new JobFacade(job));
	}
	@Override
	protected void asyncRun() {
		Date dt = new Date();
		for (JobFacade job : this.jobs) {
			this.workers.addElement(new QuartzJob(dt, job));
		}
	}
	/***
	 * 关停
	 */
	public void shutdown() {
		this.workers.shutdownNow();
		if (! jobs.isEmpty())this.cancel();
	}
}
