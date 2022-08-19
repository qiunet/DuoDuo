package org.qiunet.utils.test.pool;

import org.qiunet.utils.pool.ObjectPool;

/***
 *
 * @author qiunet
 * 2022/8/19 11:40
 */
public class TempObj {
	public static final ObjectPool<TempObj> POOL = new ObjectPool<TempObj>() {
		@Override
		protected TempObj newObject(Handle<TempObj> handler) {
			return new TempObj(handler);
		}
	};
	private final ObjectPool.Handle<TempObj> handle;

	private int val;
	private TempObj(ObjectPool.Handle<TempObj> handle) {
		this.handle = handle;
	}

	public static TempObj valueOf(int val) {
		TempObj tempObj = POOL.get();
		tempObj.val = val;
		return tempObj;
	}

	public void recycle() {
		this.val = 0;
		this.handle.recycle();
	}

	public int getVal() {
		return val;
	}
}
