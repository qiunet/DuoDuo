package org.qiunet.utils.args;

import com.google.common.collect.Maps;

import java.util.Map;

/***
 *
 *
 * @author qiunet
 * 2020-10-16 10:49
 */
public class ArgsDataMap<T> {

	private final Map<ArgumentKey<T>, Argument<T>> dataMap = Maps.newConcurrentMap();

	public Argument<T> get(ArgumentKey<T> key) {
		return dataMap.get(key);
	}

	public void remove(ArgumentKey<?> key) {
		dataMap.remove(key);
	}

	public void clear(){
		dataMap.clear();
	}

	public Argument<T> computeIfAbsent(ArgumentKey<T> key) {
		return dataMap.computeIfAbsent(key, key0 -> {
			Argument<T> argument = key0.newAttribute();
			argument.setAllArgsRef(this);
			return argument;
		});
	}
}
