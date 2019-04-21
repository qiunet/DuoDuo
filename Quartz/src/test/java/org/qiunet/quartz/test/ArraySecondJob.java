package org.qiunet.quartz.test;

import org.qiunet.quartz.BaseJob;

public class ArraySecondJob extends BaseJob {

	@Override
	protected Boolean doWork() throws Exception {
		logger.info("ArraySecondJob called");
		return true;
	}

	@Override
	protected boolean logExecInfo() {
		return false;
	}

	@Override
	public String cronExpression() {
		return "0/3 * * * * ?";
	}
}
