package org.qiunet.function.attr.cfg;

import com.google.common.collect.Maps;
import org.qiunet.function.attr.enums.IAttrEnum;
import org.qiunet.utils.json.JsonUtil;

import java.util.Map;
import java.util.Set;

/**
 * 属性map的包装类.
 * 子类继承. 然后实现parse方法
 * @param <Type>
 */
public abstract class AttrWrapper<Type extends Enum<Type> & IAttrEnum<Type>, WRAPPER extends AttrWrapper<Type, WRAPPER>> {
	protected final Map<Type, Long> attrs = Maps.newHashMap();

	public AttrWrapper() {
		this(Maps.newHashMap());
	}

	public AttrWrapper(WRAPPER fightAttr) {
		this(fightAttr.attrs);
	}

	public AttrWrapper(Map<Type, Long> attrs) {
		this.attrs.putAll(attrs);
	}

	/**
	 * 增加某个属性
	 * @param type
	 * @param val
	 */
	public void addAttr(Type type, long val) {
		this.attrs.merge(type, val, Long::sum);
	}

	/**
	 * 获得属性map
	 * @return
	 */
	public Map<Type, Long> getAttrs() {
		return attrs;
	}

	/**
	 * 设定某个属性
	 * @param type
	 * @param val
	 */
	public void put(Type type, long val) {
		this.attrs.put(type, val);
	}
	/**
	 * 合并某个FightAttr
	 * @param fightAttr
	 */
	public void merge(WRAPPER fightAttr) {
		fightAttr.attrs.forEach(this::addAttr);
	}

	/**
	 * 扣减某个类型的数值
	 * @param type
	 * @param val
	 */
	public void deduct(Type type, long val) {
		if (val < 1) {
			throw new IllegalArgumentException("Val can not less than 0");
		}
		if (! attrs.containsKey(type)) {
			throw new IllegalArgumentException("Not have type ["+type+"]");
		}

		if (attrs.get(type) - val < 0) {
			throw new IllegalArgumentException("Not enough for deduct! need ["+val+"] but last ["+attrs.get(type)+"]");
		}

		this.addAttr(type, -val);
		if (attrs.get(type) < 0) {
			attrs.remove(type);
		}
	}

	public Set<Type> keySet(){
		return attrs.keySet();
	}

	/**
	 * 从当前的属性扣减另一个fightAttr
	 * @param fightAttr
	 */
	public void deduct(WRAPPER fightAttr) {
		fightAttr.attrs.forEach(this::deduct);
	}

	public Long getAttr(Type type) {
		return attrs.getOrDefault(type, 0L);
	}

	public boolean container(Type type) {
		return attrs.containsKey(type);
	}


	/***
	 * 构造一个proto的map
	 * @return
	 */
	public Map<Integer, Long> buildProtoMap() {
		Map<Integer, Long> retMap = Maps.newHashMap();
		attrs.forEach((attrType, val) -> retMap.put(attrType.type(), val));
		return retMap;
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonString(attrs);
	}
}
