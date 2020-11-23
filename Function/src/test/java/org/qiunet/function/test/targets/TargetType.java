package org.qiunet.function.test.targets;

import org.qiunet.function.targets.ITargetType;

/***
 *
 *
 * @author qiunet
 * 2020-11-23 17:38
 */
public enum TargetType implements ITargetType {

	LEVEL("等级目标"),
	/**
	 * 怪物类型由 ITargetParam 指定
	 */
	KILL_BOSS("杀怪目标"),
	;

	private String desc;

	TargetType(String desc) {
		this.desc = desc;
	}

	@Override
	public String desc() {
		return desc;
	}
}
