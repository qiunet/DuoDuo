package org.qiunet.cfg.test;

import org.qiunet.cfg.base.INestListConfig;

/***
 * Excel 的内容
 键	值1	开始时间(10位时间戳)	结束时间(10位时间戳)
 id	val1	val2	val3
 ALL	SERVER	ALL	ALL
 1111	1,2,3	123456	1.1
 2222	3,4,5	123457	1.2
 3333	5,6,7	123458	1.3
 */
public class Init3Cfg implements INestListConfig<Integer> {
	private int id;
	private String val1;
	private long val2;
	private double val3;

	public String getVal1() {
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
}
