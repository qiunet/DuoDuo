package org.qiunet.quartz;

/**
 * 一个线程调度
 */
public interface IJob {

	/**
	 * 执行
	 */
	void doJob();
	/****
	 *  没有年这个选项. 因为没有用过
	 *
	 * 格式   * * * * * *
	 * 对应 秒 分 时 日 月 周
	 *
	 * 秒 分 0 - 59
	 * 时 0 - 23
	 * 日 1- 31
	 * 月 1 - 12
	 * 周 1- 7
	 *
	 * 每个格式可以支持
	 * 指定  0 * * * * * 每分钟执行
	 * 		0,30 * * * * * 0秒和30秒时候执行
	 * 区间  0 0-5 * * * *  每小时的前5分钟执行
	 * 间隔	0 0/5 * * * *  第零分开始每5分钟执行一次.
	 * @return
	 */
	String cronExpression();
}
