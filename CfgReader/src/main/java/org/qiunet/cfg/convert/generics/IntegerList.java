package org.qiunet.cfg.convert.generics;

import org.qiunet.utils.collection.safe.SafeList;

import java.util.Collection;

/***
 * covert 没法转换泛型对象.
 *  * 自定一个
 * @author qiunet
 * 2020/3/11 08:24
 **/
public class IntegerList extends SafeList<Integer> {

	public IntegerList(int initialCapacity) {
		super(initialCapacity);
	}

	public IntegerList() {
	}

	public IntegerList(Collection<? extends Integer> c) {
		super(c);
	}
}
