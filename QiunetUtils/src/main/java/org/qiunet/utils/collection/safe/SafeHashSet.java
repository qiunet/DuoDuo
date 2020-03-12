package org.qiunet.utils.collection.safe;

import java.util.*;

/**
 * 不改变类的情况下. 将集合可以定义为不可修改的集合.
 * 用于配置等地方.
 * @author qiunet
 *         Created on 17/3/1 16:42.
 */
public class SafeHashSet<E> implements Set<E>, ISafeCollection {

	private Set<E> set;
	public SafeHashSet() {
		this.set = new HashSet<>();
	}

	public SafeHashSet(Collection<? extends E> c) {
		this.set = new HashSet<>(c);
	}

	public SafeHashSet(int initialCapacity, float loadFactor) {
		this.set = new HashSet<>(initialCapacity, loadFactor);
	}

	public SafeHashSet(int initialCapacity) {
		this.set = new HashSet<>(initialCapacity);
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return set.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return set.iterator();
	}

	@Override
	public Object[] toArray() {
		return set.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return set.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return set.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.set.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.set.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.set.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.set.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.set.removeAll(c);
	}

	@Override
	public void clear() {
		this.set.clear();
	}

	@Override
	public void convertToUnmodifiable() {
		this.set = Collections.unmodifiableSet(set);
	}
}
