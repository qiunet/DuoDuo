package org.qiunet.function.attr;

import com.google.common.collect.Maps;
import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.function.attr.tree.AttrBox;

import java.util.Collections;
import java.util.Map;

/***
 *工具类
 *
 * @author qiunet
 * 2020-11-16 11:54
 */
public final class AttrUtil {

	private AttrUtil(){}

	/**
	 * 计算两个map的改变集合.
	 * old存在 new不存在. 取负
	 * old不存在 new存在. 取正
	 * old存在 new存在. 取差值
	 *
	 * @param oldMap 原有属性
	 * @param newMap 新属性
	 * @param <Attr>
	 * @return
	 */
	public static <Attr extends Enum<Attr> & IAttrEnum<Attr>> Map<Attr, Long> diff(Map<Attr, Long> oldMap, Map<Attr, Long> newMap) {
		if (oldMap == null) oldMap = Collections.emptyMap();
		if (newMap == null) newMap = Collections.emptyMap();

		Map<Attr, Long> finalNewMap = newMap;
		Map<Attr, Long> diff = Maps.newHashMapWithExpectedSize((int) (1.5 * Math.max(oldMap.size(), newMap.size())));
		oldMap.forEach((type, val) -> diff.put(type, finalNewMap.getOrDefault(type, 0L) - val));
		newMap.forEach(diff::putIfAbsent);
		removeZeroValue(diff);
		return diff;
	}

	/**
	 * 将新增的属性加入 attrMap. 并计算改变值
	 *
	 * @param box attr box
	 * @param addAttr 增加的属性值(一般配置读取)
	 * @param change 改变值map
	 * @param <Attr> 属性枚举类
	 * @return
	 */
	public static <Attr extends Enum<Attr> & IAttrEnum<Attr>> void mergeToBox(AttrBox<Attr> box, Map<Attr, Long> addAttr, Map<Attr, Long> change) {
		addAttr.forEach((type, value) -> {
			if (value == 0) {
				return;
			}
			type.valueType().merge(box.getTotalAttrs(), change, type, value);
		});
	}

	/**
	 * 合并两个map
	 * @param fromMap 需要合并的map
	 * @param toMap 合并目标map
	 * @param <Attr> 属性枚举类.
	 */
	public static <Attr extends Enum<Attr> & IAttrEnum<Attr>> void mergeMap(Map<Attr, Long> fromMap, Map<Attr, Long> toMap){
		fromMap.forEach((type, value) -> {
			if (value == 0) {
				return;
			}
			toMap.merge(type, value, Long::sum);
		});
	}
	/**
	 * 清理val == 0 的数据
	 * @param map
	 * @param <Attr>
	 */
	public static <Attr extends Enum<Attr> & IAttrEnum<Attr>> void removeZeroValue(Map<Attr, Long> map){
		map.entrySet().removeIf(en -> en.getValue() == 0);
	}
}
