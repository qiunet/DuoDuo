package org.qiunet.quartz.test;

import org.qiunet.quartz.IJob;

public class SecondJob implements IJob {

	@Override
	public void doJob() {
		System.out.println("i-m called");
	}

	@Override
	public String cronExpression() {
		return "* * * * * *";
	}
}
