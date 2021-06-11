package org.qiunet.utils.collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.stream.Collectors;

/***
 *
 * 计数map
 *
 * @author qiunet
 * 2020-08-28 08:30
 **/
public class CounterMap<Key> {

	private final Map<Key, Integer> counters = Maps.newConcurrentMap();

	/**
	 * 获得计数数量
	 * @param key
	 * @return
	 */
	public int getCount(Key key) {
		return counters.getOrDefault(key, 0);
	}
	/**
	 * 自增指定值
	 * @param key
	 * @param alter
	 * @return
	 */
	public int increase(Key key, int alter) {
		Preconditions.checkArgument(alter > 0, "alter ["+alter+"] must grant than zero!");
		return counters.merge(key, alter, Integer::sum);
	}

	/**
	 * 自增1
	 * @param key
	 * @return
	 */
	public int increase(Key key) {
		return increase(key, 1);
	}

	/**
	 * 自减指定值
	 * @param key
	 * @param alter
	 * @return
	 */
	public int decrease(Key key, int alter) {
		Preconditions.checkArgument(alter > 0, "alter ["+alter+"] must grant than zero!");
		Preconditions.checkArgument(getCount(key) >= alter, "alter number is greater than current");
		Integer integer = counters.merge(key, -alter, Integer::sum);
		if (integer <= 0) {
			counters.remove(key);
		}
		return integer;
	}

	/**
	 * 自减1
	 * @param key
	 * @return
	 */
	public int decrease(Key key) {
		return decrease(key, 1);
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
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
