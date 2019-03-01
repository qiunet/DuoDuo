package org.qiunet.utils.collection;

import java.util.*;

/***
 * 固定长度的list
 * 添加大于制定长度后, 会把最开始的remove
 * @param <E>
 */
public class LRUList<E> implements List<E> {

	private LinkedList<E> datas;

	private int length = 10;

	public LRUList(int length) {
		this.length = length;
		if (length < 1) {
			throw new IllegalArgumentException("Length can not less than 1");
		}
		this.datas = new LinkedList<>();
	}

	@Override
	public int size() {
		return this.datas.size();
	}

	@Override
	public boolean isEmpty() {
		return this.datas.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.datas.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.datas.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.datas.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.datas.toArray(a);
	}

	@Override
	public boolean add(E e) {
		boolean ret = this.datas.add(e);
		if (size() > length) {
			this.datas.poll();
		}
		return ret;
	}

	@Override
	public boolean remove(Object o) {
		return this.datas.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.datas.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.datas.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.datas.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.datas.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.datas.retainAll(c);
	}

	@Override
	public void clear() {
		this.datas.clear();
	}

	@Override
	public E get(int index) {
		return this.datas.get(index);
	}

	@Override
	public E set(int index, E element) {
		return this.datas.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		this.datas.add(index, element);
	}

	@Override
	public E remove(int index) {
		return this.datas.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.datas.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.datas.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.datas.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return this.datas.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return this.datas.subList(fromIndex, toIndex);
	}
}
