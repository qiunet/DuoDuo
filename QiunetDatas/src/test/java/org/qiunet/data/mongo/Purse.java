package org.qiunet.data.mongo;

/***
 *
 * @author qiunet
 * 2024/2/22 16:49
 ***/
public class Purse {

	private long m1;

	private long m2;

	public static Purse valueOf(long m1, long m2){
		Purse data = new Purse();
	    data.m1 = m1;
		data.m2 = m2;
		return data;
	}

	public long getM1() {
		return m1;
	}

	public long getM2() {
		return m2;
	}
}
