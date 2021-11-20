package org.qiunet.data.core.support.redis;

import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.timer.TimerManager;
import redis.clients.jedis.params.SetParams;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/***
 * redis 分布式锁.
 * 调用方式:
 *  try (RedisLock lock = RedisUtil.getInstance().redisLock(key)) {
 * 		if (lock.lock()) {
 * 		 	// do something
 * 		}
 *  }
 *  或者
 *  RedisLock lock = RedisUtil.getInstance().redisLock(key)
 *  try {
 *      if (lock.lock()) {
 *          // do something
 *      }
 *  }catch(Exception e) {
 *
 *  }finally {
 *      lock.unlock();
 *  }
 *
 */
public class RedisLock implements AutoCloseable {
	private final IRedisUtil redisUtil;
	private Future<Long> future;
	private final String key;

	RedisLock(IRedisUtil redisUtil, String key) {
		this.redisUtil = redisUtil;
		this.key = key+".lock";
	}

	/**
	 * 在try里面调用
	 * @return 是否获得锁
	 */
	private boolean lock0(){
		String ret = redisUtil.returnJedis().set(key, "", SetParams.setParams().ex(30L).nx());
		boolean locked = "OK".equals(ret);
		if (locked) {
			this.prolongedTime();
			return true;
		}
		return false;
	}

	/**
	 * 获取锁
	 * @return
	 */
	public boolean lock() throws IOException {
		if (lock0()) {
			return true;
		}

		for (int i = 0; i < 5; i++) {
			DFuture<Boolean> lockFuture = TimerManager.executor.scheduleWithDelay(this::lock0, 50, TimeUnit.MILLISECONDS);
			try {
				if (lockFuture.get()) {
					return true;
				}
			} catch (InterruptedException | ExecutionException e) {
				throw new IOException(e);
			}
		}
		throw new IOException("Redis lock timeout!");
	}

	/**
	 * 延时
	 */
	private void prolongedTime(){
		DFuture<Long> dFuture = TimerManager.executor.scheduleWithDelay(() -> redisUtil.returnJedis().expire(key, 30L),
			20, TimeUnit.SECONDS);
		dFuture.whenComplete((res , e) -> {
			if (! future.isCancelled()) {
				this.prolongedTime();
			}
		});
		this.future = dFuture;
	}

	/***
	 * 在finally里面调用
	 */
	public void unlock() {
		if (this.future != null) {
			this.future.cancel(true);
			redisUtil.returnJedis().expire(key, 0L);
		}
	}

	@Override
	public void close() {
		this.unlock();
	}
}
