package org.qiunet.utils.collection.generics;

import org.qiunet.utils.collection.safe.SafeHashSet;

import java.util.Collection;

/***
 * covert 没法转换泛型对象.
 * 自定一个
 *
 * @author qiunet
 * 2020/3/11 08:23
 **/
public class StringSet extends SafeHashSet<String> {
	public StringSet() {
	}

	public StringSet(Collection<? extends String> c) {
		super(c);
	}

	public StringSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public StringSet(int initialCapacity) {
		super(initialCapacity);
	}
}
