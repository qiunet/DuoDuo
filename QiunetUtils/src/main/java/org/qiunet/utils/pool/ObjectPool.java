package org.qiunet.utils.pool;


import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
	protected abstract T newObject(Handle<T> handler);

	/**
	 * 线程域的size
	 * @return
	 */
	public int threadScopeSize() {
		DStack<T> tdStack = stackThreadLocal.get();
		int sum = tdStack.asyncRecycleMap.values().stream().mapToInt(LinkedBlockingDeque::size).sum();
		return tdStack.stack.size + sum;
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
				this.scannerSpecifyThread();
			}
			if (stack.isEmpty()) {
				if (! this.scannerAllThread()) {
					return null;
				}
			}
			return stack.poll();
		}

		/**
		 * 对某些指定的线程回收
		 * @return
		 */
		private void scannerSpecifyThread() {
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
		private boolean scannerAllThread() {
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
