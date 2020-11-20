package org.qiunet.function.attr.manager;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.attr.tree.AttrBox;

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
	public static <Attr extends Enum<Attr> & IAttrEnum<Attr>> AttrBox<Attr> buildAttrBox() {
		return AttrManager0.instance.buildAttrBox();
	}
	/**
	 * 打印attrTree
	 */
	public static void printAttrTree(){
		AttrManager0.instance.printAttrTree();
	}
}
