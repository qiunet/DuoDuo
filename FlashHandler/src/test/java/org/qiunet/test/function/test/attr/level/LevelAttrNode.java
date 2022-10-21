package org.qiunet.test.function.test.attr.level;

import org.qiunet.function.attr.tree.IAttrNodeType;

/***
 * 等级属性节点
 * @author qiunet
 * 2020-11-20 17:00
 */
public enum LevelAttrNode implements IAttrNodeType {
	LEVEL_ROOT(null, "等级根节点"),
	;

	private final Class<?> keyClass;
	private final String desc;

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
