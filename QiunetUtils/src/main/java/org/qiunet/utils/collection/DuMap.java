package org.qiunet.utils.collection;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Set;

/***
 *
 * 双向map
 * key val 可以互查 一般用到游戏开始的缓存. 很多东西需要互查.
 * key val 必须都是可以作为map key的对象. 且值不能重复.
 *
 * @author qiunet
 * 2020-08-24 22:44
 **/
public class DuMap<Key, Val> {
	private BiMap<Key, Val> map1;
	private BiMap<Val, Key> map2;


	public DuMap(int expectedSize){
		this.map1 = HashBiMap.create(expectedSize);
		this.map2 = this.map1.inverse();
	}

	public DuMap(){
		this(16);
	}

	/**
	 * 添加一堆key val
	 * @param key
	 * @param val
	 */
	public DuMap<Key, Val> put(Key key, Val val) {
		if (this.containsKey(key)) {
			throw new IllegalArgumentException("Key ["+key.toString()+"] is repeated");
		}

		if (this.containsVal(val)) {
			throw new IllegalArgumentException("Val ["+val.toString()+"] is repeated");
		}

		map1.put(key, val);
		return this;
	}

	/**
	 * 对象中.是否有key值
	 * @param key
	 * @return
	 */
	public boolean containsKey(Key key) {
		return map1.containsKey(key);
	}

	/**
	 * 对象中是否有该val值.
	 * @param val
	 * @return
	 */
	public boolean containsVal(Val val) {
		return map2.containsKey(val);
	}

	/**
	 * 通过val得到key
	 * @param val
	 * @return
	 */
	public Key getKey(Val val) {
		return map2.get(val);
	}

	/**
	 * 通过key 得到val
	 * @param key
	 * @return
	 */
	public Val getVal(Key key) {
		return map1.get(key);
	}

	/**
	 * 是否为空
	 * @return
	 */
	public boolean isEmpty(){
		return map1.isEmpty();
	}

	/**
	 * 得到长度
	 * @return
	 */
	public int size(){
			return map1.size();
	}

	/**
	 * 返回所有的key
	 * @return
	 */
	public Set<Key> keys(){
		return this.map1.keySet();
	}

	/**
	 * 返回所有的val
	 * @return
	 */
	public Set<Val> vals() {
		return this.map2.keySet();
	}
}
