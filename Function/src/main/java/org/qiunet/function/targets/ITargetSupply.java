package org.qiunet.function.targets;

import java.util.function.BiConsumer;

/***
 *
 * Target 相关的service 实现该接口
 *
 * @author qiunet
 * 2020-11-24 07:59
 **/
public interface ITargetSupply {
	/**
	 * 获取TargetDefGetter
	 * @param id
	 * @return
	 */
	ITargetDefGetter getTargetGetter(int id);

	/***
	 * 更新回调
	 * @return
	 */
	BiConsumer<Targets, Target> updateCallback();
}
