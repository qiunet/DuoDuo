package org.qiunet.utils.collection.safe;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.*;

/**
 * 不改变类的情况下. 将集合可以定义为不可修改的集合.
 *  * 用于配置等地方.
 * @author qiunet
 * Created on 17/3/1 16:35.
 */
public class SafeMap<KEY, VAL> implements Map<KEY, VAL>, ISafeCollection {
	private final Logger logger = LoggerType.DUODUO.getLogger();
	private Map<KEY, VAL> map;
	/***
	 * 缺失打印.
	 */
	private boolean loggerAbsent;

	public SafeMap(int initialCapacity, float loadFactor) {
		this.map = new LinkedHashMap<>(initialCapacity, loadFactor);
	}

	public SafeMap(int initialCapacity) {
		this.map = new LinkedHashMap<>(initialCapacity);
	}

	public SafeMap() {
		this.map = new LinkedHashMap<>();
	}

	public SafeMap(Map<? extends KEY, ? extends VAL> m) {
		this.map = new LinkedHashMap<>(m);
	}

	public SafeMap(int initialCapacity, float loadFactor, boolean accessOrder) {
		this.map = new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder);
	}


	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public VAL get(Object key) {
		VAL val =  map.get(key);
		if (loggerAbsent && val == null) {
			logger.error("=================Key ["+key+"] is not in map.=============== ");
		}
		return val;
	}

	@Override
	public VAL put(KEY key, VAL value) {
		return map.put(key, value);
	}

	@Override
	public VAL remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends KEY, ? extends VAL> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<KEY> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<VAL> values() {
		return map.values();
	}

	@Override
	public Set<Entry<KEY, VAL>> entrySet() {
		return map.entrySet();
	}

	public void loggerIfAbsent(){
		this.loggerAbsent = true;
	}

	@Override
	public void convertToUnmodifiable() {
		this.map = Collections.unmodifiableMap(map);
	}
}
