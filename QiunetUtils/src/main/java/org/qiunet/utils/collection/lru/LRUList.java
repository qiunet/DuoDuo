package org.qiunet.utils.collection.lru;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/***
 * 固定长度的list
 * 添加大于制定长度后, 会把最开始的remove
 * @param <E>
 */
public class LRUList<E> implements List<E> {
	private final Lock lock = new ReentrantLock();

	private final LinkedList<E> data;

	private int length = 10;

	public LRUList(int length) {
		this.length = length;
		if (length < 1) {
			throw new IllegalArgumentException("Length can not less than 1");
		}
		this.data = new LinkedList<>();
	}

	@Override
	public int size() {
		try {
			lock.lock();
			return this.data.size();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		try {
			lock.lock();
			return this.data.isEmpty();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean contains(Object o) {
		try {
			lock.lock();
			return this.data.contains(o);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public Iterator<E> iterator() {
		try {
			lock.lock();
			return this.data.iterator();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public Object[] toArray() {
		try {
			lock.lock();
			return this.data.toArray();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public <T> T[] toArray(T[] a) {
		try {
			lock.lock();
			return this.data.toArray(a);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean add(E e) {
		try {
			lock.lock();
			boolean ret = this.data.add(e);
			if (size() > length) {
				this.data.poll();
			}
			return  ret;
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean remove(Object o) {
		try {
			lock.lock();
			return this.data.remove(o);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		try {
			lock.lock();
			return this.data.containsAll(c);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		try {
			lock.lock();
			return this.data.addAll(c);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		try {
			lock.lock();
			return this.data.addAll(index, c);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		try {
			lock.lock();
			return this.data.removeAll(c);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		try {
			lock.lock();
			return this.data.retainAll(c);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public void clear() {
		try {
			lock.lock();
			this.data.clear();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public E get(int index) {
		try {
			lock.lock();
			return this.data.get(index);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public E set(int index, E element) {
		try {
			lock.lock();
			return this.data.set(index, element);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public void add(int index, E element) {
		try {
			lock.lock();
			this.data.add(index, element);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public E remove(int index) {
		try {
			lock.lock();
			return this.data.remove(index);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public int indexOf(Object o) {
		try {
			lock.lock();
			return this.data.indexOf(o);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public int lastIndexOf(Object o) {
		try {
			lock.lock();
			return this.data.lastIndexOf(o);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public ListIterator<E> listIterator() {
		try {
			lock.lock();
			return this.data.listIterator();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		try {
			lock.lock();
			return this.data.listIterator(index);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		try {
			lock.lock();
			return this.data.subList(fromIndex, toIndex);
		}finally {
			lock.unlock();
		}
	}
}
