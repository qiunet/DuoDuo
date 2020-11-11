package org.qiunet.utils.collection.generics;

import org.qiunet.utils.collection.safe.SafeList;

import java.util.Collection;

/***
 *covert 没法转换泛型对象.
 *  * 自定一个
 *
 * @author qiunet
 * 2020/3/11 08:25
 **/
public class StringList extends SafeList<String> {

	public StringList(int initialCapacity) {
		super(initialCapacity);
	}

	public StringList() {
	}

	public StringList(Collection<? extends String> c) {
		super(c);
	}
}
