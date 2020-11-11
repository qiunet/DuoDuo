package org.qiunet.utils.collection.generics;

import org.qiunet.utils.collection.safe.SafeHashSet;

import java.util.Collection;

/***
 *covert 没法转换泛型对象.
 *  * 自定一个
 *
 * @author qiunet
 * 2020/3/11 08:23
 **/
public class LongSet extends SafeHashSet<Long> {
	public LongSet() {
	}

	public LongSet(Collection<? extends Long> c) {
		super(c);
	}

	public LongSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public LongSet(int initialCapacity) {
		super(initialCapacity);
	}
}
