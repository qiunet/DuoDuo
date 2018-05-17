package org.qiunet.quartz.test;

import org.qiunet.quartz.IJob;

public class ArraySecondJob implements IJob {
	@Override
	public void doJob() {
		System.out.println("ArraySecondJob called");
	}

	@Override
	public String cronExpression() {
		return "0/3 * * * * *";
	}
}
