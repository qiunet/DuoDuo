package org.qiunet.utils.pool;

public interface Pool<T> {

	/**
	 * 从池里拿出来一个
	 * @return
	 */
	public T getFromPool();
	/**
	 * 回收对象
	 * @param t
	 */
	public void recycle(T t);

	/**
	 * 得到最大的空闲值
	 * @return 值
	 */
	public int getMaxIdle();
	/***
	 * 得到允许的最小空闲
	 * @return 值
	 */
	public int getMinIdle();
	/**
	 * 得到允许的最大活跃
	 * @return 值
	 */
	public int getMaxActive();
	
	/**
	 * 得到等待超时时间 ms
	 * @return
	 */
	public int getMaxWaitTimeout();
	/**
	 * 返回idel count
	 * @return
	 */
	public int getIdelCount();
	/**
	 * 得到当前活跃数
	 * @return 值
	 */
	public int getActiveCount();
	/**
	 * 返回打印
	 * @return
	 */
	public String toString();
}
