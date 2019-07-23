package org.qiunet.data.core.support.redis;

import org.qiunet.utils.timer.TimerManager;

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
	private String key;
	private boolean locked;
	private Future<Void> future;
	private IRedisUtil redisUtil;

	RedisLock(IRedisUtil redisUtil, String key) {
		this.redisUtil = redisUtil;
		this.key = key;
	}

	/**
	 * 在try里面调用
	 * @return
	 */
	public boolean lock(){
		String ret = redisUtil.returnJedis().set(key, "", "nx", "ex", 30);
		this.locked ="OK".equals(ret);
		if (this.locked) {
			this.prolongedTime();
		}
		return this.locked;
	}

	private void prolongedTime(){
		this.future = TimerManager.getInstance().scheduleWithDeley(() -> {
			redisUtil.returnJedis().expire(key, 30);
			prolongedTime();
			return null;
		}, 20, TimeUnit.SECONDS);
	}

	/***
	 * 在finally里面调用
	 */
	public void unlock() {
		this.close();
	}

	@Override
	public void close() {
		if (this.future != null) {
			this.future.cancel(true);
			redisUtil.returnJedis().expire(key, 0);
		}
	}
}
