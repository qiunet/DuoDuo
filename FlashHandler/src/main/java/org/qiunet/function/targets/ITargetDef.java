package org.qiunet.function.targets;

/***
 * 任务的定义
 *
 * @author qiunet
 * 2020-11-23 12:58
 */
public interface ITargetDef {
	/**
	 * 任务类型
	 * @return
	 */
	<T extends Enum<T> & ITargetType> T getTargetType();

	/**
	 * 任务参数
	 * @return
	 */
	String getTargetParam();

	/**
	 * 完成任务的参数
	 * @return
	 */
	long getValue();

	/**
	 * 获得任务的ID
	 * @return
	 */
	Integer getId();
}
