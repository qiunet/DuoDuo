package org.qiunet.function.test.attr.level;

import org.qiunet.function.attr.tree.IAttrNodeType;

/***
 *
 *
 * @author qiunet
 * 2020-11-20 17:00
 */
public enum LevelAttrNode implements IAttrNodeType {
	LEVEL_ROOT(null, "等级根节点"),
	;

	private Class<?> keyClass;
	private String desc;

	LevelAttrNode(Class<?> keyClass, String desc) {
		this.keyClass = keyClass;
		this.desc = desc;
	}

	@Override
	public Class<?> keyClass() {
		return keyClass;
	}

	@Override
	public String desc() {
		return desc;
	}
}
