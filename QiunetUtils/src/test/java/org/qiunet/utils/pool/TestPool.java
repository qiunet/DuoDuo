package org.qiunet.utils.pool;

import org.apache.http.client.HttpClient;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;
import org.junit.Assert;
import org.qiunet.utils.data.KeyValueData;
import org.qiunet.utils.http.HttpsClientPool;
import org.qiunet.utils.math.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * @author qiunet
 *         Created on 16/11/5 08:34.
 */
public class TestPool extends BaseTest{
	@Test
	public void exec() {
		int maxActive = 10;
		int maxIdle = 5;
		int minIdle = 2;
		Properties properties = new Properties();
		properties.setProperty("HttpsClientPool.maxActive", String.valueOf(maxActive));
		properties.setProperty("HttpsClientPool.maxIdle", String.valueOf(maxIdle));
		properties.setProperty("HttpsClientPool.minIdle", String.valueOf(minIdle));
		HttpsClientPool pool = new HttpsClientPool(new KeyValueData(properties));

		List<HttpClient> clients = new ArrayList();
		for (int i = 0 ; i < maxActive; i++){
			clients.add(pool.getFromPool());
		}
		Assert.assertTrue(pool.getActiveCount() == maxActive);
		Assert.assertTrue(pool.getIdelCount() == 0);

		for (HttpClient client : clients) {
			pool.recycle(client);
		}
		Assert.assertTrue(pool.getActiveCount() == 0);
		Assert.assertTrue(pool.getIdelCount() == Math.min(maxIdle, maxActive));
	}
	@Test
	public void testMultThread(){
		int maxActive = 100;
		int maxIdle = 10;
		int minIdle = 5;
		Properties properties = new Properties();
		properties.setProperty("HttpsClientPool.maxActive", String.valueOf(maxActive));
		properties.setProperty("HttpsClientPool.maxIdle", String.valueOf(maxIdle));
		properties.setProperty("HttpsClientPool.minIdle", String.valueOf(minIdle));
		final HttpsClientPool pool = new HttpsClientPool(new KeyValueData(properties));
		
		int threadCount = 10;
		final int loopCount = 5;
		final CountDownLatch latch = new CountDownLatch(threadCount * loopCount);
		for (int i = 0 ; i < threadCount; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0 ; j < loopCount; j++){
						HttpClient client = pool.getFromPool();
						try {
							Thread.sleep(MathUtil.random(10));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						pool.recycle(client);
						logger.info(pool.toString());
						latch.countDown();
					}
				}
			}, "-thread--"+i).start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info(pool.toString());
		Assert.assertTrue(pool.getActiveCount() == 0);
		Assert.assertTrue(pool.getIdelCount() == pool.getMaxIdle());
	}
}
