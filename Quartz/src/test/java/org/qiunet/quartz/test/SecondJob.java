package org.qiunet.quartz.test;

import org.qiunet.quartz.BaseJob;

public class SecondJob extends BaseJob {

	@Override
	public Boolean doWork() {
		logger.info("i-m called");
		return true;
	}

	@Override
	protected boolean logExecInfo() {
		return false;
	}

	@Override
	public String cronExpression() {
		return "* * * * * ?";
	}
}
