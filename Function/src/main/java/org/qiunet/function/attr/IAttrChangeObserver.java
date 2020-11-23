package org.qiunet.function.attr;

import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.attr.tree.AttrRoad;
import org.qiunet.listener.observer.IObserver;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-11-16 12:47
 */
@FunctionalInterface
public interface IAttrChangeObserver<Attr extends Enum<Attr> & IAttrEnum<Attr>> extends IObserver {
	/**
	 * 返回改变的类型以及数值
	 * @param changed
	 */
	 void attrChange(AttrRoad road, Map<Attr, Long> changed);
}
