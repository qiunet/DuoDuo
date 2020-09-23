package org.qiunet.utils.data;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现的一个Properties map 的封装类
 * @author qiunet
 *         Created on 16/12/21 07:52.
 */
public class KeyValueData<K , V> implements IKeyValueData<K , V> {
	private Logger logger = LoggerType.DUODUO.getLogger();
	private Map<K, V> map = new HashMap<>();

	/***
	 * 构建一个空的Keyval map
	 */
	public KeyValueData() {
		this.load(new HashMap<>());
	}

	public KeyValueData(Map<K, V> map) {
		this.load(map);
	}
	/***
	 * 热加载, 重新替换map
	 * @param map
	 */
	protected void load(Map<K, V> map){
		if (map == null) throw new NullPointerException("Map can not be null for KeyValueData");
		this.map = map;
	}
	/**
	 * 提出合并
	 * @param map
	 */
	protected void merge(Map<K, V> map){
		if (map == null) throw new NullPointerException("Map can not be null for KeyValueData");
		this.map.putAll(map);
	}

	@Override
	public Map<K, V> returnMap() {
		return map;
	}

	@Override
	public boolean containKey(K key) {
		return map.containsKey(key);
	}

	@Override
	public V getValue(K k) {
		return map.get(k);
	}


	@Override
	public String getString(K key) {
		V v = getValue(key);
		if (v == null) {
			// 这里需要打印缺少的key.
			logger.error("=================Key ["+key+"] is not in map.=============== ");
			return null;
		}
		return v.toString();
	}
}
