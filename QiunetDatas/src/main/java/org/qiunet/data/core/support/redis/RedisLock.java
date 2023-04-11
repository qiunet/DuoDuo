package org.qiunet.data.core.support.redis;

import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.timer.TimerManager;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/***
 * redis 分布式锁.
 * 调用方式:
 *  try (RedisLock lock = RedisUtil.getInstance().redisLock(key)) {
 * 		if (lock.tryLock()) {
 * 		 	// do something
 * 		}
 *  }
 *  或者
 *  RedisLock lock = RedisUtil.getInstance().redisLock(key)
 *  try {
 *      if (lock.tryLock()) {
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
	private DFuture<Long> future;
	private final String key;
	private final String val;

	RedisLock(IRedisUtil redisUtil, String key) {
		this.val = StringUtil.randomString(5);
		this.redisUtil = redisUtil;
		this.key = key+".lock";
	}

	/**
	 * 在try里面调用
	 * @return 是否获得锁
	 */
	private boolean lock0(){
		String ret = redisUtil.returnJedis().set(key, val, SetParams.setParams().ex(30L).nx());
		boolean locked = "OK".equals(ret);
		if (locked) {
			this.prolongedTime();
			return true;
		}
		return false;
	}

	/**
	 * 获取锁 200 ms 没有获取到返回false
	 * @return true 获取到了, false 没有
	 */
	public boolean tryLock() {
		return tryLock(200, TimeUnit.MILLISECONDS);
	}
	/**
	 * 根据指定时间尝试获取锁
	 * @return true 获取到了, false 没有
	 */
	public boolean tryLock(int time, TimeUnit unit) {
		if (lock0()) {
			return true;
		}

		if (time <= 0) {
			throw new CustomException("Wait time value error!");
		}

		if (unit.toSeconds(time) >= 60) {
			throw new CustomException("Wait time can great than 60 seconds!");
		}

		int lastTime = (int) unit.toMillis(time), count = 0;
		while (lastTime > 0) {
			int delayTime = (10 + count) * (1+ ++count / 4);
			delayTime = Math.min(lastTime, delayTime);
			DFuture<Boolean> lockFuture = TimerManager.executor.scheduleWithDelay(this::lock0, delayTime, TimeUnit.MILLISECONDS);
			try {
				if (lockFuture.get()) {
					return true;
				}
			} catch (InterruptedException | ExecutionException e) {
				throw new CustomException(e, "redis lock: ");
			}
			lastTime -= delayTime;
		}
		return false;
	}

	/**
	 * 延时
	 */
	private void prolongedTime(){
		this.future = TimerManager.executor.scheduleWithDelay(() -> redisUtil.returnJedis().expire(key, 30L),
			20, TimeUnit.SECONDS);
		this.future.whenComplete((res , e) -> {
			if (! future.isCancelled()) {
				this.prolongedTime();
			}
		});
	}

	/***
	 * 在finally里面调用
	 */
	public void unlock() {
		redisUtil.execCommands(jedis -> {
			String val = jedis.get(key);
			if (this.val.equals(val)) {
				if (this.future != null) {
					this.future.cancel(true);
				}
				jedis.del(key);
			}
			return null;
		});
	}

	@Override
	public void close() {
		this.unlock();
	}
}
