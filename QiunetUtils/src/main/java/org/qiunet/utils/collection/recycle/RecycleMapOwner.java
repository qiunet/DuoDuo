package org.qiunet.utils.collection.recycle;

import org.qiunet.utils.pool.ObjectPool;

import java.util.HashMap;

/***
 * 可以回收的list
 *
 * @author qiunet
 * 2022/11/24 17:09
 */
public class RecycleMapOwner<K, V> {
	private final ObjectPool<RecycleMap<K, V>> RECYCLE;

	public RecycleMapOwner() {
		this(256, 64);
	}
	public RecycleMapOwner(int threadMaxCapacity, int queueCapacityForPerThread) {
		this.RECYCLE = new ObjectPool<RecycleMap<K, V>>(threadMaxCapacity, queueCapacityForPerThread) {
			@Override
			protected RecycleMap<K, V> newObject(Handle<RecycleMap<K, V>> handler) {
				return new RecycleMap<>(handler);
			}
		};
	}
	/**
	 * 获得一个可以回收的list
	 * @return
	 */
	public RecycleMap<K, V> get() {
		return RECYCLE.get();
	}

	public static class RecycleMap<K, V> extends HashMap<K, V> {
		private final ObjectPool.Handle<RecycleMap<K, V>> handler;

		private RecycleMap(ObjectPool.Handle<RecycleMap<K, V>> handler) {
			this.handler = handler;
		}
		/**
		 * 回收
		 */
		public void recycle() {
			this.clear();
			this.handler.recycle();
		}
	}
}
