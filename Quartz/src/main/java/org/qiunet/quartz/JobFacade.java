package org.qiunet.quartz;

import java.util.Date;

class JobFacade {

	private IJob job;

	private CronExpressionData expressionData;

	public JobFacade(IJob job) {
		this.job = job;

		this.expressionData = new CronExpressionData(job.cronExpression());
	}




	public void doJob(Date dt) {
		if (this.expressionData.isValid(dt)) {
			this.job.doJob();
		}
	}


}
