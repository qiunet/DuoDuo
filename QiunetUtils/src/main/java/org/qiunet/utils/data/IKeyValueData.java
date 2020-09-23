package org.qiunet.utils.data;

import java.util.Map;

/**
 * 封装一层 properties 比较好取值. 好打印错误
 * @author qiunet
 *         Created on 16/12/20 21:33.
 */
public interface IKeyValueData<K, V> {
	/**
	 * 是否包含key
	 * @param key
	 * @return
	 */
	boolean containKey(K key);

	/***
	 * 得到map
	 * @return
	 */
	Map<K, V> returnMap();
	/**
	 * 获得 value
	 * @param k
	 * @return
	 */
	 V getValue(K k);

	/**
	 * 获取. 如果没有使用默认值
	 * @param key
	 * @return
	 */
	 default V getOrDefault(K key, V defaultVal){
	 	return returnMap().getOrDefault(key, defaultVal);
	 }

	/**
	 * 返回key的值
	 * @param key
	 * @return 默认返回 ""
	 */
	 String getString(K key);
	/**
	 * 获取key的值
	 * @param key
	 * @param defaultVal 如果没有返回的默认值
	 * @return
	 */
	 default String getString(K key,String defaultVal){
		 String rt=defaultVal;
		 V v = getValue(key);
		 if(v != null){
			 rt=v.toString().trim();
		 }
		 return rt;
	 }

	/**
	 * 获得int数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	default int getInt(K key,int defaultVal){
		if (! containKey(key)) {
			return defaultVal;
		}
		return getInt(key);
	}
	/**
	 * 获得int数据配置
	 * @param key
	 * @return
	 */
	default int getInt(K key){
		return Integer.parseInt(getString(key));
	}

	/**
	 * 获得float数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	default float getFloat(K key,float defaultVal){
		if (! containKey(key)) {
			return defaultVal;
		}
		return getFloat(key);
	}
	/**
	 * 获得float数据配置
	 * @param key
	 * @return
	 */
	default float getFloat(K key){
		return Float.parseFloat(getString(key));
	}
	/**
	 * 获得long数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	default long getLong(K key,long defaultVal){
		if (! containKey(key)) {
			return defaultVal;
		}
		return getLong(key);
	}
	/**
	 * 获得long数据配置
	 * @param key
	 * @return
	 */
	default long getLong(K key){
		return Long.parseLong(getString(key));
	}
	/**
	 * 获得double数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	default double getDouble(K key,double defaultVal){
		if (! containKey(key)) {
			return defaultVal;
		}
		return getDouble(key);
	}
	/**
	 * 获得double数据配置
	 * @param key
	 * @return
	 */
	default double getDouble(K key){
		return Double.parseDouble(getString(key));
	}

	/***
	 * 得到boolean 值. 默认false
	 * @param key
	 * @return
	 */
	default boolean getBoolean(K key){
		if (! containKey(key)) {
			return false;
		}

		return "true".equals(getString(key));
	}
}
