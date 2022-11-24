package org.qiunet.utils.collection.recycle;

import org.qiunet.utils.pool.ObjectPool;

import java.util.ArrayList;

/***
 * 可以回收的list
 *
 * @author qiunet
 * 2022/11/24 17:09
 */
public class RecycleListOwner<E> {
	private final ObjectPool<RecycleList<E>> RECYCLE;

	public RecycleListOwner() {
		this(256, 64);
	}

	public RecycleListOwner(int threadMaxCapacity, int queueCapacityForPerThread) {
		this.RECYCLE = new ObjectPool<RecycleList<E>>(threadMaxCapacity, queueCapacityForPerThread) {
			@Override
			protected RecycleList<E> newObject(Handle<RecycleList<E>> handler) {
				return new RecycleList<>(handler);
			}
		};
	}

	/**
	 * 获得一个可以回收的list
	 * @return
	 */
	public RecycleList<E> get() {
		return RECYCLE.get();
	}

	public static class RecycleList<E> extends ArrayList<E> {
		private final ObjectPool.Handle<RecycleList<E>> handler;

		private RecycleList(ObjectPool.Handle<RecycleList<E>> handler) {
			this.handler = handler;
		}
		/**
		 * 回收
		 */
		public void recycle() {
			this.clear();
			this.trimToSize();
			this.handler.recycle();
		}
	}
}
