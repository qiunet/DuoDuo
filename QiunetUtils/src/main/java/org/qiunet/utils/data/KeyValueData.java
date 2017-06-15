package org.qiunet.utils.data;

import org.qiunet.utils.string.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 实现的一个Properties map 的封装类
 * @author qiunet
 *         Created on 16/12/21 07:52.
 */
public class KeyValueData<K , V> implements IKeyValueData<K , V> {
	private Map<K, V> map = new HashMap<>();
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
	protected void merge(Map map){
		if (map == null) throw new NullPointerException("Map can not be null for KeyValueData");
		this.map.putAll(map);
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
	public String getString(K key, String defaultVal) {
		String rt=defaultVal;
		V v = map.get(key);
		if(v != null){
			rt=v.toString().trim();
		}
		return rt;
	}

	@Override
	public String getString(K key) {
		return getString(key , "");
	}

	@Override
	public short getShort(K key, int defaultVal) {
		return Short.parseShort(getString(key, String.valueOf(defaultVal)));
	}

	@Override
	public short getShort(K key) {
		return getShort(key, 0);
	}

	@Override
	public int getInt(K key, int defaultVal) {
		return Integer.parseInt(getString(key, String.valueOf(defaultVal)));
	}

	@Override
	public int getInt(K key) {
		return getInt(key, 0);
	}

	@Override
	public byte getByte(K key, int defaultVal) {
		return Byte.parseByte(getString(key , String.valueOf(defaultVal)));
	}

	@Override
	public byte getByte(K key) {
		return getByte(key , 0);
	}

	@Override
	public float getFloat(K key, float defaultVal) {
		return Float.parseFloat(getString(key , String.valueOf(defaultVal)));
	}

	@Override
	public float getFloat(K key) {
		return getFloat(key ,0f);
	}

	@Override
	public long getLong(K key, long defaultVal) {
		return Long.parseLong(getString(key , String.valueOf(defaultVal)));
	}

	@Override
	public long getLong(K key) {
		return getLong(key , 0L);
	}

	@Override
	public double getDouble(K key, double defaultVal) {
		return Double.parseDouble(getString(key , String.valueOf(defaultVal)));
	}

	@Override
	public double getDouble(K key) {
		return getDouble(key , 0d);
	}
}
