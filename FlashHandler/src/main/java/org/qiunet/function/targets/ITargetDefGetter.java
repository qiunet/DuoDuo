package org.qiunet.function.targets;

import java.util.List;

/***
 * 一个任务组的内容.
 * 任务组包含一个任务列表
 *
 * @author qiunet
 * 2020-11-23 15:07
 */
@FunctionalInterface
public interface ITargetDefGetter<TargetDef extends ITargetDef> {
	/**
	 * 获得指定index 的目标配置
	 * @param tid
	 * @return
	 */
	TargetDef getTargetDef(int tid);
}
