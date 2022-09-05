package org.qiunet.function.targets;

import java.util.List;

/***
 * Targets 的 list 获取
 *
 * @author qiunet
 * 2022/9/6 11:47
 */
public interface ITargetsDefGetter<TargetDef extends ITargetDef>  {
	/**
	 * 获得targets的Target list
	 * @return list
	 */
	List<TargetDef> targets();
}
