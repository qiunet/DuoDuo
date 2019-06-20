package org.qiunet.cfg.test;

import org.qiunet.cfg.base.ISimpleMapConfig;

/***
 * Excel 的内容
 *
 * KEY	VALUE	VALUE	VALUE
 * int	string	long	double
 * 1111	1,2,3	123456	1.1
 * 2222	3,4,5	123457	1.2
 * 3333	5,6,7	123458	1.3
 */
public class InitWithRewardCfg implements ISimpleMapConfig<Integer> {
	private int id;
	private RewardData val;
	private long val2;
	private double val3;

	public RewardData getReward() {
		return val;
	}

	public long getVal2() {
		return val2;
	}

	public double getVal3() {
		return val3;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
