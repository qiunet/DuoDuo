package org.qiunet.utils.collection.generics;

import org.qiunet.utils.collection.safe.SafeList;

import java.util.Collection;

/***
 * covert 没法转换泛型对象.
 *  * 自定一个
 * @author qiunet
 * 2020/3/11 08:24
 **/
public class LongList extends SafeList<Long> {

	public LongList(int initialCapacity) {
		super(initialCapacity);
	}

	public LongList() {
	}

	public LongList(Collection<? extends Long> c) {
		super(c);
	}
}
