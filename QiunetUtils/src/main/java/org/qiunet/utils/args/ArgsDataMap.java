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

	private Map<ArgumentKey<T>, Argument<T>> datas = Maps.newConcurrentMap();

	public Argument<T> get(ArgumentKey<T> key) {
		return datas.get(key);
	}

	public void remove(ArgumentKey key) {
		datas.remove(key);
	}

	public void clear(){
		datas.clear();
	}

	public Argument<T> computeIfAbsent(ArgumentKey<T> key) {
		return datas.computeIfAbsent(key, key0 -> {
			Argument<T> argument = key0.newAttribute();
			argument.setAllArgsRef(this);
			return argument;
		});
	}
}
