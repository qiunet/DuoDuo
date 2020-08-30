package org.qiunet.utils.collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/***
 *
 * 计数map
 *
 * @author qiunet
 * 2020-08-28 08:30
 **/
public class CounterMap<Key> {

	private Map<Key, AtomicInteger> counters = Maps.newConcurrentMap();

	/**
	 * 获得计数数量
	 * @param key
	 * @return
	 */
	public int getCount(Key key) {
		AtomicInteger atomicInteger = counters.get(key);
		return atomicInteger == null ? 0 : atomicInteger.get();
	}
	/**
	 * 自增指定值
	 * @param key
	 * @param alter
	 * @return
	 */
	public int incr(Key key, int alter) {
		Preconditions.checkArgument(alter > 0, "alter ["+alter+"] must grant than zero!");
		AtomicInteger counter = counters.computeIfAbsent(key, k -> new AtomicInteger());
		return counter.addAndGet(alter);
	}

	/**
	 * 自增1
	 * @param key
	 * @return
	 */
	public int incr(Key key) {
		return incr(key, 1);
	}

	/**
	 * 自减指定值
	 * @param key
	 * @param alter
	 * @return
	 */
	public int decr(Key key, int alter) {
		Preconditions.checkArgument(alter > 0, "alter ["+alter+"] must grant than zero!");
		AtomicInteger counter = counters.computeIfAbsent(key, k -> new AtomicInteger());
		return counter.addAndGet(-alter);
	}

	/**
	 * 自减1
	 * @param key
	 * @return
	 */
	public int decr(Key key) {
		return decr(key, 1);
	}

	/**
	 * 删除指定 keys
	 * @param keys
	 */
	public void remove(Key ... keys) {
		for (Key key : keys) {
			counters.remove(key);
		}
	}
	/**
	 * 转成map
	 * @return
	 */
	public Map<Key, Integer> toMap() {
		return this.counters.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, en -> en.getValue().get()));
	}
}
