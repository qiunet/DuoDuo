package org.qiunet.utils.data;
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
	public boolean containKey(K key);
	/**
	 * 获得 value
	 * @param k
	 * @return
	 */
	public V getValue(K k);
	/**
	 * 获取key的值
	 * @param key
	 * @param defaultVal 如果没有返回的默认值
	 * @return
	 */
	public String getString(K key,String defaultVal);

	/**
	 * 返回key的值
	 * @param key
	 * @return 默认返回 ""
	 */
	public String getString(K key);

	/**
	 * 返回short
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public short getShort(K key,int defaultVal);

	/**
	 * 返回short
	 * @param key
	 * @return
	 */
	public short getShort(K key);
	/**
	 * 获得int数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public int getInt(K key,int defaultVal);
	/**
	 * 获得int数据配置
	 * @param key
	 * @return
	 */
	public int getInt(K key);
	/**
	 * 获得byte数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public byte getByte(K key,int defaultVal);
	/**
	 * 获得byte数据配置
	 * @param key
	 * @return
	 */
	public byte getByte(K key);
	/**
	 * 获得float数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public float getFloat(K key,float defaultVal);
	/**
	 * 获得float数据配置
	 * @param key
	 * @return
	 */
	public float getFloat(K key);
	/**
	 * 获得long数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public long getLong(K key,long defaultVal);
	/**
	 * 获得long数据配置
	 * @param key
	 * @return
	 */
	public long getLong(K key);
	/**
	 * 获得double数据配置
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public double getDouble(K key,double defaultVal);
	/**
	 * 获得double数据配置
	 * @param key
	 * @return
	 */
	public double getDouble(K key);
}
