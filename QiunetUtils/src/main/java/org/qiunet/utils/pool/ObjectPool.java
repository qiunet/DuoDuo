package org.qiunet.utils.pool;


import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.utils.system.OSUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/***
 * 一个简单的对象池
 *
 * @author qiunet
 * 2022/8/15 15:29
 */
public abstract class ObjectPool<T> {
	private static final ThreadLocal<Map<DStack<?> , UnreliablyList<?>>> DELAYED_QUEUE = ThreadLocal.withInitial(HashMap::new);
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
	protected abstract T newObject(Handle<T> handler);

	/**
	 * 线程域的size
	 * @return
	 */
	public int threadScopeSize() {
		DStack<T> tdStack = stackThreadLocal.get();
		int sum = tdStack.asyncRecycleMap.values().stream().mapToInt(UnreliablyList::size).sum();
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
		return tdStack.asyncRecycleMap.values().stream().mapToInt(UnreliablyList::size).sum();
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
		Node<T> next;

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

	private static final class DLinkedList<T> {
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

		public boolean full() {
			return this.lastCapacity() <= 0;
		}

		public int lastCapacity() {
			return maxCapacity - size;
		}

		/**
		 * 从 {@link UnreliablyList}回收数据
		 * @param head0 list的head
		 * @param tail0 list的tail
		 * @param size0 list的size
		 */
		public void add(Node<T> head0, Node<T> tail0, int size0) {
			if (isEmpty()) {
				this.head = head0;
			}else {
				this.tail.next = head0;
			}
			this.tail = tail0;
			this.size += size0;
		}

		/***
		 * 本地回收 node
		 * @param node 需要回收的node
		 */
		public void add(Node<T> node) {
			if (lastCapacity() <= 0) {
				return;
			}

			if (isEmpty()) {
				head = tail = node;
			}else {
				tail.next = node;
				tail = node;
			}
			size ++;

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
			}
			temp.next = null;
			size --;
			return temp;
		}

		public boolean isEmpty() {
			return size == 0;
		}
	}

	private static final class DStack<T> {
		/**
		 * 异步回收的数据
		 */
		final Map<Thread, UnreliablyList<T>> asyncRecycleMap = Maps.newConcurrentMap();
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
			for (UnreliablyList<T> list : asyncRecycleMap.values()) {
				if (this.stack.full()) {
					break;
				}

				if (list.size() < 5) {
					continue;
				}

				list.consumerAndReset(stack::add);
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
			Map<DStack<?>, UnreliablyList<?>> map = DELAYED_QUEUE.get();
			UnreliablyList<T> list = (UnreliablyList<T>) map.get(this);
			Thread currentThread = Thread.currentThread();
			if (list == null) {
				list = new UnreliablyList<>();
				this.asyncRecycleMap.put(currentThread, list);
				map.put(this, list);
			}

			if (list.size() >= queueCapacityForPerThread) {
				// drop object
				return;
			}
			list.add(obj);
		}
	}

	/**
	 * 不可靠的linked list
	 * 并发可能丢失数据
	 * @param <E>
	 */
	private static class UnreliablyList<E> {

		Node<E> head, tail;

		private int size;

		public synchronized void add(Node<E> node) {
			if (tail == null) {
				head = tail = node;
			}else {
				tail.next = node;
				tail = node;
			}
			size ++;
		}

		/**
		 * 消费掉现有的数据, 然后重置
		 * @param consumer 消费者
		 */
		public void consumerAndReset(TConsumer<Node<E>,Node<E>, Integer> consumer) {
			if (size == 0) {
				return;
			}

			Node<E> tempHead, tempTail;
			int tempSize;
			synchronized (this) {
				tempTail = tail;
				tempHead = head;
				tempSize = size;
				head = tail = null;
				size = 0;
			}
			consumer.accept(tempHead, tempTail, tempSize);
		}

		public int size() {
			return size;
		}
	}

	@FunctionalInterface
	private interface TConsumer<O, T, S> {
		void accept(O o, T t, S s);
	}
}
