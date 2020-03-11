package org.qiunet.utils.collection.safe;

import org.qiunet.utils.exceptions.SafeColletionsModifyException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author qiunet
 *         Created on 17/3/1 16:28.
 */
public class SafeList<E> extends ArrayList<E> {
	/**
	 * 一个只允许初始化一次的锁变量
	 */
	private boolean safeLock;

	public SafeList(){
		super();
	}

	/**
	 * 指定一个初始化大小的list
	 * @param initialCapacity
	 */
	public SafeList(int initialCapacity){
		super(initialCapacity);
	}

	public SafeList(Collection<? extends E> c) {
		super(c);
	}

	@Override
	public boolean add(E e) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.add(e);
	}

	@Override
	public void clear() {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		super.clear();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.addAll(c);
	}

	@Override
	public E set(int index, E element) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.set(index, element);
	}

	/**
	 * 把当前的list设置为锁定状态. 不允许修改里面的数据
	 */
	public void safeLock() {
		this.trimToSize();
		this.safeLock = true;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");

		super.removeRange(fromIndex, toIndex);
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

	@Override
	public E remove(int index) {
		if (safeLock)
			throw new SafeColletionsModifyException("It locked, Can not set again!");
		return super.remove(index);
	}
}
