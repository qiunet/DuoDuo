package org.qiunet.function.attr.life;

import com.google.common.base.Preconditions;
import org.qiunet.utils.exceptions.CustomException;

import java.util.concurrent.locks.ReentrantLock;

/***
 * 一些范围值的工具类.
 * 比如HP  MP
 *
 * @Author qiunet
 * @Date 2021/4/25 22:51
 **/
public class LongRangeValue {
	private final ReentrantLock lock = new ReentrantLock();
	private final boolean holdIfReduceToMinimal;
	private final IValueChange valueChange;
	private final long minimal;
	private long current;
	private long maximal;
	/**
	 * 构造函数
	 *
	 * @param current 初始的当前值.
	 * @param maximal 最大值
	 * @param minimal 最小值
	 * @param holdIfReduceToMinimal 当减到最小时候时候 hold住. 不让操作.
	 * @param valueChange 值变化外部处理.
	 */
	public LongRangeValue(long current, long maximal, long minimal,
					  boolean holdIfReduceToMinimal, IValueChange valueChange) {
		this.holdIfReduceToMinimal = holdIfReduceToMinimal;
		this.valueChange = valueChange;
		this.current = current;
		this.maximal = maximal;
		this.minimal = minimal;
	}

	/**
	 * 当前是否有被hold住
	 * @return 当前是否是最小值并且不能被改变.
	 */
	private boolean isHoldByMinimal(){
		return this.holdIfReduceToMinimal && isMinimal();
	}

	/**
	 * 是否达到最小值了.
	 * @return 是否是最小值
	 */
	public boolean isMinimal(){
		return this.current <= minimal;
	}

	/**
	 * 是否已经最大值
	 * @return 是否是最大值
	 */
	public boolean isMaximal(){
		return this.current >= maximal;
	}

	/**
	 * 是否是最大值
	 * @return 是否是最大值
	 */
	public boolean isFull(){
		return isMaximal();
	}

	/**
	 * 扣减一个值. 如果最终到达最低值. 则设置为minVal
	 * @param value 减少指定的数值.
	 * @param minVal 需要设置的minVal
	 * @return 是否有改变
	 */
	public boolean decreaseAndHoldMin(long value, long minVal) {
		return this.decreaseAndHoldMin(value, () -> {
			this.setCurrent(minVal);
		});
	}

	/**
	 * 重新改变hold状态.
	 * @param val 需要设置的值
	 */
	public void reOpen(long val) {
		if (! isHoldByMinimal()) {
			return;
		}

		this.setCurrent(val);
	}

	/**
	 * 重置最大值, 当前值按照比例重新设置.
	 * @param maximal 重新设置最大值.
	 */
	public void resetMaximal(long maximal) {
		Preconditions.checkArgument(maximal > this.minimal);
		int rct = getRct();
		this.maximal = maximal;
		this.setCurrentWithRct(rct);
	}
	/**
	 * 设置当前值
	 * @param value 设置当前值
	 */
	public void setCurrent(long value) {
		if (this.current == value) {
			return;
		}

		long change = value - this.current;
		if (change > 0) {
			this.increase(change);
		}else {
			this.decreaseAndHoldMin(change, minimal + 1);
		}
	}


	/**
	 * 按照万分比设置当前值
	 * @param rct 万分比
	 */
	public void setCurrentWithRct(int rct) {
		long val = maximal * rct / 10000;
		this.setCurrent(val);
	}

	/**
	 * 按照百分比设置当前值
	 * @param pct 百分比
	 */
	public void setCurrentWithPct(int pct) {
		long val = maximal * pct / 100;
		this.setCurrent(val);
	}

	/**
	 * 尝试改变当前值
	 * @param val 设置的值
	 * @return 是否有改变
	 */
	private boolean tryChangeCurrentVal(long val) {
		if (val == this.current) {
			return false;
		}
		this.current = val;
		return true;
	}
	/**
	 * 新增一个值.
	 *
	 * @param value
	 * @return
	 */
	public boolean increase(long value) {
		if (value < 0) {
			throw new CustomException("value [{}] is negatives number!", value);
		}

		if (value == 0) {
			return false;
		}

		lock.lock();
		try {
			if (isHoldByMinimal()) {
				return false;
			}

			this.current += value;
			this.valueChange.handle(this.current);
		}finally {
			lock.unlock();
		}
		return true;
	}

	/**
	 * 减少一定值
	 * @param value
	 * @param touchMinimalValue
	 * @return
	 */
	public boolean decreaseAndHoldMin(long value, ITouchMinimalValue touchMinimalValue) {
		if (value < 0) {
			throw new CustomException("value [{}] is negatives number!", value);
		}

		if (value == 0) {
			return false;
		}

		lock.lock();
		try {
			if (isMinimal()) {
				return false;
			}

			this.current = Math.max(this.minimal, this.current - value);
			if (isMinimal()) {
				touchMinimalValue.handle();
			}
		}finally {
			lock.unlock();
		}

		return true;
	}

	/**
	 * 获得百分比. 往上取整
	 * @return
	 */
	public int getPct(){
		int rct = getRct();
		return Math.max(1, rct / 100);
	}

	/**
	 * 获取万分比 往上取整
	 * @return
	 */
	public int getRct() {
		if (maximal == 0) {
			return 0;
		}
		int rct =  (int) ((current * 10000) / maximal);
		return Math.max(1, rct);
	}


	/**
	 * 当前值的变动通知
	 */
	@FunctionalInterface
	public interface IValueChange {
		/**
		 * 处理.
		 * @param value
		 */
		void handle(long value);
	}

	/**
	 * 当到达最小值时候, 需要的处理
	 * 比如第一个杀死boss的处理.
	 */
	@FunctionalInterface
	public interface ITouchMinimalValue {
		/**
		 * 处理
		 */
		void handle();
	}
}
