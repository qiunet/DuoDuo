package org.qiunet.utils.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现的一个Properties map 的封装类
 * @author qiunet
 *         Created on 16/12/21 07:52.
 */
public class KeyValueData<K , V> implements IKeyValueData<K , V> {
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

	@Override
	public Map<K, V> returnMap() {
		return map;
	}
}
