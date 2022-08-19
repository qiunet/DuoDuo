package org.qiunet.utils.test.pool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.pool.ThreadScopeObjectPool;
import org.qiunet.utils.thread.ThreadPoolManager;

/***
 *
 * @author qiunet
 * 2022/8/19 11:32
 */
public class TestObjectPool {
	private final ThreadScopeObjectPool<Object> pool = new ThreadScopeObjectPool<>(Object::new);

	@BeforeEach
	public void clear() {
		pool.clear();
	}

	@Test
	public void testThreadScopeRecycle() {
		Object o1 = pool.get();
		Object o2 = pool.get();
		pool.recycle(o2);
		pool.recycle(o1);

		Object o3 = pool.get();
		Object o4 = pool.get();

		Assertions.assertSame(o3, o1);
		Assertions.assertSame(o2, o4);
		pool.recycle(o4);
		pool.recycle(o3);

		Assertions.assertEquals(2, pool.threadScopeSize());
	}


	@Test
	public void testAsyncObjectPool() throws InterruptedException {
		TempObj tempObj1 = TempObj.valueOf(1);
		TempObj tempObj2 = TempObj.valueOf(2);
		ThreadPoolManager.NORMAL.execute(() -> {
			tempObj2.recycle();
			tempObj1.recycle();
		});

		Thread.sleep(5);
		Assertions.assertEquals(0, TempObj.POOL.threadScopeStackSize());
		Assertions.assertEquals(2, TempObj.POOL.asyncThreadRecycleSize());

		TempObj tempObj3 = TempObj.valueOf(3);
		TempObj tempObj4 = TempObj.valueOf(4);

		Assertions.assertSame(tempObj3, tempObj2);
		Assertions.assertSame(tempObj4, tempObj1);

		tempObj3.recycle();
		tempObj4.recycle();

		Assertions.assertEquals(2, TempObj.POOL.threadScopeStackSize());
		Assertions.assertEquals(2, TempObj.POOL.threadScopeSize());
	}
}
