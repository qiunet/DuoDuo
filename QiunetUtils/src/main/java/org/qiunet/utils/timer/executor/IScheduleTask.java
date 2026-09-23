package org.qiunet.utils.timer.executor;

public interface IScheduleTask {
	/**
	 * 任务计划执行的绝对时间戳 (毫秒, DateUtil)
	 */
	long getTimestamp();

	/**
	 * 创建时的延迟毫秒
	 */
	long getDelay();
}
