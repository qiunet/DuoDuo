package org.qiunet.utils.pool;


import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.utils.system.OSUtil;

import java.lang.ref.WeakReference;
import java.util.*;

/***
 * 一个简单的对象池
 *
 * @author qiunet
 * 2022/8/15 15:29
 */
public abstract class ObjectPool<T> {
	private static final ThreadLocal<Map<DStack<?> , SwitchList<Node>>> DELAYED_QUEUE = ThreadLocal.withInitial(HashMap::new);
	private final ThreadLocal<DStack<T>> stackThreadLocal;

	public ObjectPool() {
		this(512, OSUtil.availableProcessors() * 3);
	}

	public ObjectPool(int maxCapacity, int queueCapacityForPerThread) {
		stackThreadLocal = ThreadLocal.withInitial(() -> new DStack<>(Thread.currentThread(), maxCapacity, queueCapacityForPerThread));
	}

	public interface Handle<T> {
		void recycle();
	}

	/**
	 * 为池构造一个新的对象
	 * @param handler
	 * @return
	 */
	protected abstract T newObject(Handle<T> handler);

	/**
	 * 线程域的size
	 * @return
	 */
	public int threadScopeSize() {
		DStack<T> tdStack = stackThreadLocal.get();
		int sum = tdStack.asyncRecycleMap.values().stream().mapToInt(List::size).sum();
		return tdStack.stack.size + sum;
	}
	/**
	 * 线程域的 stack size
	 * @return
	 */
	public int threadScopeStackSize() {
		DStack<T> tdStack = stackThreadLocal.get();
		return tdStack.stack.size;
	}
	/**
	 * 其它线程回收数
	 * @return
	 */
	public int asyncThreadRecycleSize() {
		DStack<T> tdStack = stackThreadLocal.get();
		return tdStack.asyncRecycleMap.values().stream().mapToInt(List::size).sum();
	}

	/**
	 * 获得对象
	 * @return
	 */
	public T get() {
		DStack<T> tdStack = stackThreadLocal.get();
		Node<T> node = tdStack.pop();
		if (node == null) {
			node = tdStack.newHandler();
			node.value = newObject(node);
			Preconditions.checkNotNull(node.value);
		}
		return node.value;
	}

	private static final class Node<T> implements Handle<T> {
		/**
		 * 是否已经回收
		 */
		private boolean recycled;
		DStack<T> stack;
		T value;
		/** 链表结构*/
		Node<T> pre, next;

		public Node(DStack<T> stack) {
			this.stack = stack;
		}

		@Override
		public void recycle() {
			if (this.recycled) {
				throw new IllegalStateException("Already recycled!");
			}
			this.recycled = true;

			stack.push(this);
		}
	}

	private static final class DLinkedList<T> extends AbstractCollection<Node<T>> {
		private final int maxCapacity;
		private Node<T> head, tail;
		private int size;

		public DLinkedList(int maxCapacity) {
			Preconditions.checkState(maxCapacity >= 10);
			this.maxCapacity = maxCapacity;
		}

		@Override
		public Iterator<Node<T>> iterator() {
			return new DIterator<>(this.head);
		}
		@Override
		public int size() {
			return size;
		}

		public boolean full() {
			return this.lastCapacity() <= 0;
		}

		public int lastCapacity() {
			return maxCapacity - size;
		}
		@Override
		public boolean add(Node<T> t) {
			if (lastCapacity() <= 0) {
				return false;
			}

			if (isEmpty()) {
				head = tail = t;
				size ++;
				return true;
			}
			tail.next = t;
			t.pre = tail;
			tail = t;
			size ++;
			return true;
		}

		public Node<T> poll() {
			if (isEmpty()) {
				return null;
			}
			Node<T> temp = head;
			if (size == 1) {
				head = tail = null;
			}else {
				head = head.next;
				head.pre = null;
			}
			temp.pre = null;
			temp.next = null;
			size --;
			return temp;
		}
	}

	private static final class DIterator<T> implements Iterator<Node<T>> {
		private Node<T> node;

		public DIterator(Node<T> node) {
			this.node = node;
		}

		@Override
		public boolean hasNext() {
			return node != null;
		}

		@Override
		public Node<T> next() {
			if (! this.hasNext())
				throw new NoSuchElementException();
			Node<T> temp = this.node;
			this.node = node.next;
			return temp;
		}
	}

	private static final class DStack<T> {
		/**
		 * 异步回收的数据
		 */
		final Map<Thread, SwitchList<Node>> asyncRecycleMap = Maps.newConcurrentMap();
		final WeakReference<Thread> threadRef;
		final int queueCapacityForPerThread;
		final DLinkedList<T> stack;
		final int maxCapacity;
		DStack(Thread thread, int maxCapacity, int queueCapacityForPerThread) {
			this.queueCapacityForPerThread = queueCapacityForPerThread;
			this.threadRef = new WeakReference<>(thread);
			this.stack = new DLinkedList<>(maxCapacity);
			this.maxCapacity = maxCapacity;
		}

		Node<T> newHandler() {
			return new Node<>(this);
		}
		/**
		 * 弹出一个对象
		 * @return
		 */
		Node<T> pop() {
			if (stack.isEmpty()) {
				if (! this.scannerAllThread()) {
					return null;
				}
			}

			if (stack.isEmpty()) {
				return null;
			}

			Node<T> node = stack.poll();
			if (node != null) {
				node.recycled = false;
			}
			return node;
		}
		/**
		 * 从其它线程的回收栈回收对象.
		 */
		private boolean scannerAllThread() {
			for (SwitchList<Node> list : asyncRecycleMap.values()) {
				if (list.isEmpty()) {
					continue;
				}
				if (this.stack.full()) {
					break;
				}
				List<Node> nodes = list.lSwitch();
				nodes.forEach(this.stack::add);
			}
			return ! this.stack.isEmpty();
		}
		/**
		 * 压入一个对象
		 * @param obj
		 */
		void push(Node<T> obj) {
			if (threadRef.get() == Thread.currentThread()) {
				this.stack.add(obj);
			}else {
				this.asyncPush(obj);
			}
		}

		/**
		 * 异步压入
		 * @param obj
		 */
		private void asyncPush(Node<T> obj) {
			Map<DStack<?>, SwitchList<Node>> map = DELAYED_QUEUE.get();
			SwitchList<Node> list = map.get(this);
			Thread currentThread = Thread.currentThread();
			if (list == null) {
				list = new SwitchList<>();
				this.asyncRecycleMap.put(currentThread, list);
				map.put(this, list);
			}

			if (list.size() >= queueCapacityForPerThread) {
				if (stack.lastCapacity() < queueCapacityForPerThread) {
					// drop object
					return;
				}
			}
			list.add(obj);
		}
	}

	/**
	 * 可以切换的list
	 * @param <E>
	 */
	public static class SwitchList<E> extends AbstractList<E> {
		private List<E> currList;

		public SwitchList() {
			this.currList = new LinkedList<>();
		}

		public List<E> lSwitch() {
			List<E> list = currList;
			this.currList = new LinkedList<>();
			return list;
		}
		@Override
		public E get(int index) {
			return currList.get(index);
		}

		@Override
		public int size() {
			return currList.size();
		}

		@Override
		public List<E> subList(int fromIndex, int toIndex) {
			return currList.subList(fromIndex, toIndex);
		}

		@Override
		public boolean add(E e) {
			return currList.add(e);
		}

		@Override
		public void clear() {
			currList.clear();
		}
	}
}
