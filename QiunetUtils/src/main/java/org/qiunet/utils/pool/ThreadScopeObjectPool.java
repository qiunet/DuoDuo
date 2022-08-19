package org.qiunet.utils.pool;

import org.qiunet.utils.exceptions.CustomException;

import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;

/***
 *
 * 本地线程缓存的对象池.
 * 仅支持同一个线程获取和回收.
 * 如果异步线程的. 请使用{@link ObjectPool}
 *
 * @author qiunet
 * 2022/8/19 09:29
 */
public class ThreadScopeObjectPool<T> {

	private final ThreadLocal<NStack<T>> threadLocal = ThreadLocal.withInitial(NStack::new);
	/**
	 * 最大对象数
	 */
	private final int maxElement;
	/**
	 * 销毁时候调用
	 */
	private final Consumer<T> onDrop;
	/**
	 * 需要新对象调用
	 */
	private final Supplier<T> getter;


	public ThreadScopeObjectPool(Supplier<T> getter) {
		this(null, getter, 10);
	}

	public ThreadScopeObjectPool(Consumer<T> onDrop, Supplier<T> getter, int maxElement) {
		this.maxElement = maxElement;
		this.onDrop = onDrop;
		this.getter = getter;
	}

	/**
	 * 获取一个对象
	 * @return
	 */
	public T get() {
		NStack<T> stack = threadLocal.get();
		if (stack.size() == 0) {
			return getter.get();
		}

		return stack.pop();
	}

	/**
	 * 线程域的size
	 * @return
	 */
	public int threadScopeSize() {
		return threadLocal.get().size();
	}
	/**
	 * 回收对象
	 */
	public void recycle(T obj) {
		NStack<T> stack = threadLocal.get();
		if (! stack.isThreadValid()) {
			if (onDrop != null) {
				onDrop.accept(obj);
			}
			throw new CustomException("Not in get thread!");
		}

		if (stack.size() >= maxElement) {
			if (onDrop != null) {
				onDrop.accept(obj);
			}
			return;
		}
		stack.push(obj);
	}

	/**
	 * 清掉所有
	 */
	public void clear() {
		if (onDrop != null) {
			threadLocal.get().stack.forEach(onDrop);
		}
		threadLocal.get().stack.clear();
	}


	public static class NStack<T>{
		/**
		 * 值
		 */
		private final Stack<T> stack = new Stack<>();
		/**
		 * 创建时候的的线程
		 */
		private final Thread t = Thread.currentThread();

		public T pop(){
			return stack.pop();
		}

		public void push(T t) {
			this.stack.push(t);
		}

		public int size() {
			return this.stack.size();
		}

		/**
		 * 线程是否有效
		 * @return
		 */
		public boolean isThreadValid() {
			return Thread.currentThread() == this.t;
		}
	}
}
