package org.qiunet.data.core.support.redis;

import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.timer.TimerManager;
import redis.clients.jedis.params.SetParams;

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
	private final String key;
	private Future<Long> future;
	private final IRedisUtil redisUtil;

	RedisLock(IRedisUtil redisUtil, String key) {
		this.redisUtil = redisUtil;
		this.key = key;
	}

	/**
	 * 在try里面调用
	 * @return
	 */
	public boolean lock(){
		String ret = redisUtil.returnJedis().set(key, "", SetParams.setParams().ex(30L).nx());
		boolean locked = "OK".equals(ret);
		if (locked) {
			this.prolongedTime();
		}
		return locked;
	}

	private void prolongedTime(){
		DFuture<Long> dFuture = TimerManager.executor.scheduleWithDelay(() -> redisUtil.returnJedis().expire(key, 30),
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
			redisUtil.returnJedis().expire(key, 0);
		}
	}

	@Override
	public void close() {
		this.unlock();
	}
}
