package org.qiunet.function.targets;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 15:07
 */
@FunctionalInterface
public interface ITargetDefGetter<TargetDef extends ITargetDef> {
	/**
	 * 获得指定index 的目标配置
	 * @param index
	 * @return
	 */
	default TargetDef getTargetDef(int index) {
		return getTargetCfg().get(index);
	}
	/**
	 * 获得任务目标配置列表
	 * @return
	 */
	TargetDefList<TargetDef> getTargetCfg();
}
