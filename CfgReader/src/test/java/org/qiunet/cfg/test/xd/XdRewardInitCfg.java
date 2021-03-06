package org.qiunet.cfg.test.xd;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.INeedInitCfg;
import org.qiunet.cfg.base.ISimpleMapCfg;
import org.qiunet.cfg.test.RewardData;

/***
 ***Excel 的内容
 * **
 * *键 值1 开始时间(10位时间戳)结束时间(10位时间戳)
 * *id val1 val2 val3
 * *ALL SERVER ALL ALL
 * *1111 1,2,3 123456 1.1
 * *2222 3,4,5 123457 1.2
 * *3333 5,6,7 123458 1.3
 *
 * @author qiunet
 * 2020-04-24 08:02
 **/
@Cfg(value = "config/init/init_data.xd", order = 3333)
public class XdRewardInitCfg implements ISimpleMapCfg<Integer>, INeedInitCfg {
	private int id;
	private RewardData val1;
	private long val2;
	private double val3;

	public RewardData getVal1() {
		return val1;
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

	@Override
	public void init() {
		val2 = Long.MAX_VALUE;
	}
}
