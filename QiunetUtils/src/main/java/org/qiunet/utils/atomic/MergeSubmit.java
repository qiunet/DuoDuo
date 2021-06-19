package org.qiunet.utils.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/***
 * 合并提交工具类
 *
 * 根据 mark类型. 判断是否能提交.
 *
 * @author qiunet
 * 2021-06-19 11:39
 */
public class MergeSubmit<Mark extends Enum<Mark>> {

	private final AtomicIntegerArray array;

	public MergeSubmit(Class<Mark> clazz) {
		this.array = new AtomicIntegerArray(clazz.getEnumConstants().length);
	}

	/**
	 * 尝试提交
	 * @param mark 提交的类型
	 * @return true 可以提交
	 */
	public boolean trySubmit(Mark mark) {
		return this.array.compareAndSet(mark.ordinal(), 0, 1);
	}

	/**
	 * 结束提交
	 * @param mark 提交类型
	 * @return 是否操作成功
	 */
	public boolean cancelSubmit(Mark mark) {
		return this.array.compareAndSet(mark.ordinal(), 1, 0);
	}
}
