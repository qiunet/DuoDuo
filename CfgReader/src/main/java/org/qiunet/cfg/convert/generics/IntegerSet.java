package org.qiunet.cfg.convert.generics;

import org.qiunet.utils.collection.safe.SafeHashSet;

import java.util.Collection;

/***
 *covert 没法转换泛型对象.
 *  * 自定一个
 *
 * @author qiunet
 * 2020/3/11 08:23
 **/
public class IntegerSet extends SafeHashSet<Integer> {
	public IntegerSet() {
	}

	public IntegerSet(Collection<? extends Integer> c) {
		super(c);
	}

	public IntegerSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public IntegerSet(int initialCapacity) {
		super(initialCapacity);
	}
}
