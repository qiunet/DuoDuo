package org.qiunet.function.targets;

/***
 * 任务的定义
 *
 * @author qiunet
 * 2020-11-23 12:58
 */
public interface ITargetDef<T extends Enum<T> & ITargetType, Param extends ITargetParam> {
	/**
	 * 任务类型
	 * @return
	 */
	T getTargetType();

	/**
	 * 任务参数
	 * @return
	 */
	Param getTargetParam();

	/**
	 * 完成任务的参数
	 * @return
	 */
	long getValue();
}
