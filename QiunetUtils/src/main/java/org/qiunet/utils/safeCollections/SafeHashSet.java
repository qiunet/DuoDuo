package org.qiunet.utils.safeCollections;

import org.qiunet.utils.exceptions.SafeColletionsModifyException;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author qiunet
 *         Created on 17/3/1 16:42.
 */
public class SafeHashSet<E> extends HashSet<E> {
	private boolean safeLock;
	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.addAll(c);
	}

	@Override
	public boolean add(E e) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.add(e);
	}

	@Override
	public boolean remove(Object o) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.removeAll(c);
	}
	/**
	 * 把当前的list设置为锁定状态. 不允许修改里面的数据
	 */
	public void safeLock() {
		this.safeLock = true;
	}
	@Override
	public void clear() {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		super.clear();
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.retainAll(c);
	}
	
}
