package org.qiunet.function.attr.enums;

import org.qiunet.function.attr.AttrValue;

import java.util.Map;
import java.util.function.Consumer;

/***
 * 属性值的类型
 *
 * @author qiunet
 * 2020-11-16 10:16
 */
public enum AttrValueType {

	base {
		@Override
		public <Attr extends Enum<Attr> & IAttrEnum<Attr>> void merge(Map<Attr, AttrValue> attrMap, Map<Attr, Long> change, Attr type, long value) {
			AttrValueType.handler(attrMap, change, (attrValue -> attrValue.alterBase(value)), type);
		}
	},

	rct {
		@Override
		public <Attr extends Enum<Attr> & IAttrEnum<Attr>> void merge(Map<Attr, AttrValue> attrMap, Map<Attr, Long> change, Attr type, long value) {
			base.merge(attrMap, change, type, value);
			Attr[] additions = type.additions();
			if (additions == null) {
				return;
			}

			for (Attr addition : additions) {
				AttrValueType.handler(attrMap, change, (attrValue -> attrValue.alterRct((int) value)), addition);
			}
		}
	},

	extra {
		@Override
		public <Attr extends Enum<Attr> & IAttrEnum<Attr>> void merge(Map<Attr, AttrValue> attrMap, Map<Attr, Long> change, Attr type, long value) {
			base.merge(attrMap, change, type, value);
			Attr[] additions = type.additions();
			if (additions == null) {
				return;
			}

			for (Attr addition : additions) {
				AttrValueType.handler(attrMap, change, (attrValue -> attrValue.alterExtraVal(value)), addition);
			}
		}
	};

	/**
	 * 添加某个属性到map
	 * @param attrMap 总属性的map
	 * @param change 变化的map
	 * @param type 属性类型
	 * @param value 属性值
	 * @param <Attr> 属性枚举类
	 */
	public abstract <Attr extends Enum<Attr> & IAttrEnum<Attr>> void merge(
		Map<Attr, AttrValue> attrMap,
		Map<Attr, Long> change,
		Attr type,
		long value
	);

	/**
	 * 处理重复代码.
	 * @param attrMap 总属性的map
	 * @param change 变化的map
	 * @param type 属性类型
	 * @param <Attr> 属性枚举类
	 */
	private static <Attr extends Enum<Attr> & IAttrEnum<Attr>> void handler(Map<Attr, AttrValue> attrMap,
						 Map<Attr, Long> change,
						 Consumer<AttrValue> exec,
						 Attr type){
		AttrValue attrValue = attrMap.computeIfAbsent(type, key -> new AttrValue());
		long oldTotalVal = attrValue.getTotalVal();
		exec.accept(attrValue);
		change.merge(type, attrValue.getTotalVal() - oldTotalVal, Long::sum);
	}
}
