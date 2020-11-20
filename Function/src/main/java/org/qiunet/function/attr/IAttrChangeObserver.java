package org.qiunet.function.attr;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.attr.tree.AttrNodeRoad;
import org.qiunet.listener.observer.IObserver;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-11-16 12:47
 */
public interface IAttrChangeObserver extends IObserver {
	/**
	 * 返回改变的类型以及数值
	 * @param changed
	 * @param <Attr>
	 */
	<Attr extends Enum<Attr> & IAttrEnum<Attr>> void attrChange(AttrNodeRoad road, Map<Attr, Long> changed);
}
