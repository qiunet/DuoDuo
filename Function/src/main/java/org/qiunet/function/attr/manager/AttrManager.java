package org.qiunet.function.attr.manager;

import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.attr.tree.AttrBox;
import org.qiunet.function.attr.tree.AttrRoad;
import org.qiunet.function.attr.tree.IAttrNodeType;

/***
 *
 *
 * @author qiunet
 * 2020-11-20 15:47
 */
public class AttrManager {
	/**
	 * 获得一个初始的attrBox
	 * @param <Attr>
	 * @return
	 */
	public static <Owner extends AbstractUserActor<Owner>, Attr extends Enum<Attr> & IAttrEnum<Attr>> AttrBox<Owner, Attr> buildAttrBox(Owner owner) {
		return AttrManager0.instance.buildAttrBox(owner);
	}
	/**
	 * 打印attrTree
	 */
	public static void printAttrTree(){
		AttrManager0.instance.printAttrTree();
	}

	/**
	 * 构造属性路径
	 * @param nodeType 节点类型
	 * @param keys 参数
	 * @return
	 */
	public static AttrRoad builderRoad(IAttrNodeType nodeType, Object ... keys) {
		return AttrManager0.instance.builderRoad(nodeType, keys);
	}
}
