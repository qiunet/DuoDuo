package org.qiunet.function.attr.tree;

import org.qiunet.function.attr.manager.AttrManager;

/***
 * 属性节点.
 * 添加到{@link AttrTree}上
 *
 * @author qiunet
 * 2020-11-16 16:48
 */
public interface IAttrNodeType {
	/**
	 * node 对应的key的class
	 * 如果没有分叉点. 就不需要传入该参数.
	 * 比如: 角色 -> 战神装备 -> 铸魂石玩法 -> 腰带
	 * 其中 角色 -> 战神装备 -> 铸魂石玩法 是确定路径. 腰带铸魂是[铸魂石玩法]变量. 则keyClass需要指出腰带的key类型, 比如枚举或者int.
	 * @return
	 */
	Class<?> keyClass();

	/**
	 * 枚举名
	 * @return
	 */
	String name();
	/**
	 * 描述
	 * @return
	 */
	String desc();

	/**
	 * 构建属性路径
	 * @param keys
	 * @return
	 */
	default AttrRoad builderRoad(Object ... keys){
		return AttrManager.builderRoad(this, keys);
	}
}
