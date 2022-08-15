package org.qiunet.utils.pool;


import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.system.OSUtil;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 一个简单的对象池
 *
 * @author qiunet
 * 2022/8/15 15:29
 */
public abstract class ObjectPool<T> {
	private static final ThreadLocal<Map<DStack<?> , LinkedBlockingDeque<Node<?>>>> DELAYED_QUEUE = ThreadLocal.withInitial(HashMap::new);
	private final ThreadLocal<DStack<T>> stackThreadLocal;

	public ObjectPool() {
		this(512, OSUtil.availableProcessors() * 2);
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
	public abstract T newObject(Handle<T> handler);
	/**
	 * 获得对象
	 * @return
	 */
	public T get() {
		DStack<T> tdStack = stackThreadLocal.get();
		Node<T> handle = tdStack.pop();
		if (handle == null) {
			handle = tdStack.newHandler();
			handle.value = newObject(handle);
		}
		return handle.value;
	}

	private static final class Node<T> implements Handle<T> {
		DStack<T> stack;
		T value;
		/** 链表结构*/
		Node<T> pre, next;

		public Node(DStack<T> stack) {
			this.stack = stack;
		}

		@Override
		public void recycle() {
			stack.push(this);
		}
	}

	private static final class DLinkedList<T> implements Collection<Node<T>> {
		private final int maxCapacity;
		private Node<T> head, tail;
		private int size;

		public DLinkedList(int maxCapacity) {
			Preconditions.checkState(maxCapacity >= 10);
			this.maxCapacity = maxCapacity;
		}

		public int size() {
			return size;
		}

		public int lastCapacity() {
			return maxCapacity - size;
		}

		public boolean isEmpty() {
			return size == 0;
		}

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
		@Override
		public boolean contains(Object o) {
			throw new CustomException("Not support!");
		}

		@Override
		public Iterator<Node<T>> iterator() {
			throw new CustomException("Not support!");
		}

		@Override
		public Object[] toArray() {
			Object[] result = new Object[size];
			int i = 0;
			Node<T> t = head;
			while (t != null) {
				result[i++] = t;
				t = t.next;
			}
			return result;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			throw new CustomException("Not support!");
		}

		@Override
		public boolean remove(Object o) {
			throw new CustomException("Not support!");
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			throw new CustomException("Not support!");
		}

		@Override
		public boolean addAll(Collection<? extends Node<T>> c) {
			throw new CustomException("Not support!");
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new CustomException("Not support!");
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new CustomException("Not support!");
		}

		@Override
		public void clear() {
			throw new CustomException("Not support!");
		}
	}

	private static final class DStack<T> {
		/**
		 * 异步回收的数据
		 */
		final Map<Thread, LinkedBlockingDeque<Node<?>>> asyncRecycleMap = Maps.newConcurrentMap();
		/**
		 * 需要回收的线程
		 */
		final Set<Thread> needRecycleThreads = Sets.newConcurrentHashSet();

		final AtomicBoolean needRecycleThread = new AtomicBoolean();
		final WeakReference<Thread> threadRef;
		final int queueCapacityForPerThread;
		final DLinkedList<T> stack;
		final int maxCapacity;
		public DStack(Thread thread, int maxCapacity, int queueCapacityForPerThread) {
			this.queueCapacityForPerThread = queueCapacityForPerThread;
			this.threadRef = new WeakReference<>(thread);
			this.stack = new DLinkedList<>(maxCapacity);
			this.maxCapacity = maxCapacity;
		}

		public Node<T> newHandler() {
			return new Node<>(this);
		}
		/**
		 * 弹出一个对象
		 * @return
		 */
		public Node<T> pop() {
			if (needRecycleThread.get()) {
				this.scannerThreadsObject();
			}
			if (stack.size() == 0) {
				if (! this.scannerObject()) {
					return null;
				}
			}
			return stack.poll();
		}

		/**
		 * 对某些指定的线程回收
		 * @return
		 */
		private void scannerThreadsObject() {
			for(Iterator<Thread> it = this.needRecycleThreads.iterator(); it.hasNext(); ) {
				LinkedBlockingDeque<Node<?>> deque = asyncRecycleMap.get(it.next());
				if (deque != null) {
					this.recycleDequeNode(deque);
				}
				it.remove();
			}

			this.needRecycleThread.set(false);
		}

		/**
		 * 从其它线程的回收栈回收对象.
		 */
		private boolean scannerObject() {
			for (LinkedBlockingDeque<Node<?>> deque : asyncRecycleMap.values()) {
				this.recycleDequeNode(deque);
			}
			return ! this.stack.isEmpty();
		}

		/**
		 * 回收某个deque的node
		 * 如果回收超出最大容量. 也清空掉对应的deque.
		 *
		 * @param deque
		 * @return
		 */
		private void recycleDequeNode(LinkedBlockingDeque<Node<?>> deque) {
			deque.drainTo((Collection) this.stack);
		}

		/**
		 * 压入一个对象
		 * @param obj
		 */
		public void push(Node<T> obj) {
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
			Map<DStack<?>, LinkedBlockingDeque<Node<?>>> map = DELAYED_QUEUE.get();
			LinkedBlockingDeque<Node<?>> deque = map.get(this);
			Thread currentThread = Thread.currentThread();
			if (deque == null) {
				deque = new LinkedBlockingDeque<>();
				this.asyncRecycleMap.put(currentThread, deque);
				map.put(this, deque);
			}

			if (deque.size() >= queueCapacityForPerThread) {
				if (stack.lastCapacity() < queueCapacityForPerThread) {
					// drop object
					return;
				}

				this.needRecycleThreads.add(currentThread);
				this.needRecycleThread.set(true);
			}
			deque.addLast(obj);
		}
	}
}
