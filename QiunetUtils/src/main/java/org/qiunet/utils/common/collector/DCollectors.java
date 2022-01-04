package org.qiunet.utils.common.collector;

import org.qiunet.utils.collection.safe.SafeList;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

/***
 * 自定义的collector
 *
 * @author qiunet
 * 2020-05-16 09:22
 **/
public final class DCollectors {
	/**
	 * 转为SafeList
	 * @param <T>
	 * @return
	 */
	public static <T> Collector<T, ?, List<T>> toSafeList() {
		return new CollectorImpl<>((Supplier<List<T>>) SafeList::new, List::add,
				(left, right) -> { left.addAll(right); return left; }, list ->  {
			((SafeList<T>) list).convertToUnmodifiable(); return list;});
	}
}
