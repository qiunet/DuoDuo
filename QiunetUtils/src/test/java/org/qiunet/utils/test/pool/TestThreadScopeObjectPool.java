package org.qiunet.utils.test.pool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.pool.ThreadScopeObjectPool;

/***
 *
 * @author qiunet
 * 2022/8/19 11:32
 */
public class TestThreadScopeObjectPool {
	private final ThreadScopeObjectPool<Object> pool = new ThreadScopeObjectPool<>(Object::new);

	@BeforeEach
	public void clear() {
		pool.clear();
	}

	@Test
	public void testRecycle() {
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


	//public static void main(String[] args) throws InterruptedException {
	//	for (int i = 0; i < 1000; i++) {
	//		TempObj tempObj = TempObj.valueOf(i);
	//		ThreadPoolManager.NORMAL.execute(tempObj::recycle);
	//		System.out.println(TempObj.POOL.threadScopeSize());
	//	}
	//}
}
