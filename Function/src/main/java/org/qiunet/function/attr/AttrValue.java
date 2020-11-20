package org.qiunet.function.attr;

import org.qiunet.utils.math.MathUtil;

/***
 * 属性值
 *
 * @author qiunet
 * 2020-11-16 10:28
 */
public final class AttrValue {
	/**
	 * 基础值
	 */
	private long base;
	/**
	 * 额外固定加成
	 */
	private long extraVal;
	/**
	 * 万分比加成
	 */
	private int rct;
	/**
	 * 总值
	 */
	private transient long totalVal;

	/**
	 * 计算总值
	 */
	private void calTotalVal(){
		this.totalVal = base + extraVal + MathUtil.getByRate(base, rct);
	}

	public void alterBase(long base) {
		this.base += base;
		this.calTotalVal();
	}

	public void alterExtraVal(long extraVal) {
		this.extraVal += extraVal;
		this.calTotalVal();
	}

	public void alterRct(int rct) {
		this.rct += rct;
		this.calTotalVal();
	}

	public long getBase() {
		return base;
	}

	public long getExtraVal() {
		return extraVal;
	}

	public int getRct() {
		return rct;
	}

	public long getTotalVal() {
		return totalVal;
	}
}
