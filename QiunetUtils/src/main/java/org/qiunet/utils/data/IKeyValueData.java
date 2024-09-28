package org.qiunet.utils.data;

import org.qiunet.utils.logger.LoggerType;

import java.util.Map;

/**
 * 封装一层 properties 比较好取值. 好打印错误
 *
 * @author qiunet
 * Created on 16/12/20 21:33.
 */
public interface IKeyValueData<K, V> {
	/**
	 * 是否包含key
	 *
	 * @param key
	 * @return
	 */
	default boolean containKey(K key) {
		return returnMap().containsKey(key);
	}

	/**
	 * 提出合并
	 *
	 * @param map
	 */
	default void merge(Map<K, V> map) {
		if (map == null) throw new NullPointerException("Map can not be null for KeyValueData");
		this.returnMap().putAll(map);
	}

	/***
	 * 得到map
	 * @return
	 */
	Map<K, V> returnMap();

	/**
	 * 获得 value
	 *
	 * @param key
	 * @return
	 */
	default V getValue(K key) {
		return returnMap().get(key);
	}

	/**
	 * 获取. 如果没有使用默认值
	 *
	 * @param key
	 * @return
	 */
	default V _getOrDefault(K key, V defaultVal) {
		return returnMap().getOrDefault(key, defaultVal);
	}

	/**
	 * 返回key的值
	 *
	 * @param key
	 * @return 默认返回 ""
	 */
	default String getString(K key) {
		V v = getValue(key);
		if (v == null) {
			// 这里需要打印缺少的key.
			LoggerType.DUODUO.error("=================Key [" + key + "] is not in map.=============== ");
			return null;
		}
		return v.toString();
	}

	/**
	 * 获取key的值
	 *
	 * @param key
	 * @param defaultVal 如果没有返回的默认值
	 * @return
	 */
	default String getString(K key, String defaultVal) {
		String rt = defaultVal;
		V v = getValue(key);
		if (v != null) {
			rt = v.toString().trim();
		}
		return rt;
	}

	default byte getByte(K key, int defaultVal) {
		return Byte.parseByte(getString(key, String.valueOf(defaultVal)));
	}

	default byte getByte(K key) {
		return Byte.parseByte(getString(key));
	}

	default short getShort(K key, int defaultVal) {
		return Short.parseShort(getString(key, String.valueOf(defaultVal)));
	}

	default short getShort(K key) {
		return Short.parseShort(getString(key));
	}

	/**
	 * 获得int数据配置
	 *
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	default int getInt(K key, int defaultVal) {
		if (!containKey(key)) {
			return defaultVal;
		}
		return getInt(key);
	}

	/**
	 * 获得int数据配置
	 *
	 * @param key
	 * @return
	 */
	default int getInt(K key) {
		return Integer.parseInt(getString(key));
	}

	/**
	 * 获得float数据配置
	 *
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	default float getFloat(K key, float defaultVal) {
		if (!containKey(key)) {
			return defaultVal;
		}
		return getFloat(key);
	}

	/**
	 * 获得float数据配置
	 *
	 * @param key
	 * @return
	 */
	default float getFloat(K key) {
		return Float.parseFloat(getString(key));
	}

	/**
	 * 获得long数据配置
	 *
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	default long getLong(K key, long defaultVal) {
		if (!containKey(key)) {
			return defaultVal;
		}
		return getLong(key);
	}

	/**
	 * 获得long数据配置
	 *
	 * @param key
	 * @return
	 */
	default long getLong(K key) {
		return Long.parseLong(getString(key));
	}

	/**
	 * 获得double数据配置
	 *
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	default double getDouble(K key, double defaultVal) {
		if (!containKey(key)) {
			return defaultVal;
		}
		return getDouble(key);
	}

	/**
	 * 获得double数据配置
	 *
	 * @param key
	 * @return
	 */
	default double getDouble(K key) {
		return Double.parseDouble(getString(key));
	}

	/***
	 * 得到boolean 值. 默认false
	 * @param key
	 * @return
	 */
	default boolean getBoolean(K key) {
		if (!containKey(key)) {
			return false;
		}

		return "true".equals(getString(key));
	}

	/**
	 * 数据变更接口
	 *
	 * @param <K>
	 * @param <V>
	 */
	@FunctionalInterface
	interface DataChangeListener<K, V> {
		void accept(IKeyValueData<K, V> data);
	}
}
