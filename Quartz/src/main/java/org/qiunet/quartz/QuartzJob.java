package org.qiunet.quartz;

import java.util.Date;

class QuartzJob implements Runnable {

	private Date dt;

	private JobFacade job;

	QuartzJob(Date dt, JobFacade job) {
		this.job = job;
		this.dt = dt;
	}


	@Override
	public void run() {
		this.job.doJob(dt);
	}
}
