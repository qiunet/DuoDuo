package org.qiunet.utils.data;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 实现的一个Properties map 的封装类
 * @author qiunet
 *         Created on 16/12/21 07:52.
 */
public class KeyValueData<K , V> implements IKeyValueData<K , V> {
	private Map<K, V> map;
	private final DataChangeListener<K, V> changeListener;

	/***
	 * 构建一个空的Keyval map
	 */
	public KeyValueData() {
		this(Maps.newHashMap());
	}

	public KeyValueData(DataChangeListener<K, V> changeListener) {
		this(Maps.newHashMap(), changeListener);
	}

	public KeyValueData(Map<K, V> map) {
		this(map, null);
	}

	public KeyValueData(Map<K, V> map, DataChangeListener<K, V> changeListener) {
		if (map == null) throw new NullPointerException("Map can not be null for KeyValueData");
		this.changeListener = changeListener;
		this.map = map;
	}
	/***
	 * 热加载, 重新替换map
	 * @param map
	 */
	protected void load(Map<K, V> map){
		if (map == null) throw new NullPointerException("Map can not be null for KeyValueData");
		this.map = map;

		if (changeListener != null) {
			changeListener.accept(this);
		}
	}

	@Override
	public Map<K, V> returnMap() {
		return map;
	}
}
