package org.qiunet.test.function.test.targets;

import org.qiunet.function.targets.ITargetType;

/***
 *
 * @author qiunet
 * 2020-11-23 17:38
 */
public enum TargetType implements ITargetType {

	LEVEL,
	/**
	 * 怪物类型由 ITargetParam 指定
	 */
	KILL_BOSS,
}
