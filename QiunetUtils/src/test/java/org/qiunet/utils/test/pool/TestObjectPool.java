package org.qiunet.utils.test.pool;

import com.google.common.collect.Queues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.pool.ThreadScopeObjectPool;
import org.qiunet.utils.system.OSUtil;

import java.util.concurrent.*;

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
		ExecutorService threadPool1 = new ThreadPoolExecutor(OSUtil.availableProcessors(), OSUtil.availableProcessors() * 2, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
		ExecutorService threadPool2 = new ThreadPoolExecutor(OSUtil.availableProcessors(), OSUtil.availableProcessors() * 2, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
		LinkedBlockingDeque<TempObj> deque = Queues.newLinkedBlockingDeque();
		CountDownLatch latch = new CountDownLatch(1000000);
		final long count = latch.getCount();
		new Thread(() -> {
			for (int i = 0; i < count; i++) {
				int finalI = i;
				threadPool1.execute(() ->  {
					deque.add(TempObj.valueOf(finalI));
				});
			}
		}).start();


		new Thread(() -> {
			for (int i = 0; i < count; i++) {
				threadPool2.execute(() -> {
					try {
						deque.take().recycle();
						latch.countDown();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				});
			}
		}).start();

		latch.await();
	}
}
