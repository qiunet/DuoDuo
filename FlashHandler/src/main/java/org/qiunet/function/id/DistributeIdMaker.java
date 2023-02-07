package org.qiunet.function.id;

import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.core.support.redis.RedisLock;
import org.qiunet.data.redis.util.DbUtil;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/***
 * 以服务组为单位, 生成分布式的ID
 * 例如: 玩家ID
 *
 * @author qiunet
 * 2023/2/7 17:08
 */
public class DistributeIdMaker {
	/**
	 * 需要的用户数量
	 */
	private final AtomicInteger counter = new AtomicInteger();
	/**
	 * 自增键getter
	 * 一般是从db根据group id 取
	 */
	private final Supplier<Integer> incrIdGetter;
	/**
	 * redis lock
	 */
	private RedisLock redisLock;
	/**
	 * 保存 到 db
	 */
	private final Runnable update;
	/**
	 * 组ID
	 */
	private final int serverGroupId;
	/**
	 * redis lock 前缀
	 */
	private final String redisLockKey;
	/**
	 * 使用到的redis
	 */
	private final IRedisUtil redisUtil;
	/**
	 *
	 * @param redisUtil redis
	 * @param incrIdGetter 一般是从db根据group id取
	 * @param update 保存到db
	 * @param serverGroupId group id
	 */
	public DistributeIdMaker(String redisLockKeyPrefix, IRedisUtil redisUtil, Supplier<Integer> incrIdGetter,
							 Runnable update, int serverGroupId) {
		this.redisLockKey = redisLockKeyPrefix + serverGroupId;
		this.incrIdGetter = incrIdGetter;
		this.serverGroupId = serverGroupId;
		this.redisUtil = redisUtil;
		this.update = update;
	}
	/**
	 * 产生一个ID
	 * @return
	 */
	public long generateId() {
		try {
			if (counter.incrementAndGet() == 1) {
				(redisLock = redisUtil.redisLock(redisLockKey)).lock();
			}
			return this.makeId();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (counter.decrementAndGet() == 0) {
				this.update.run();
				redisLock.unlock();
			}
		}
	}

	/**
	 * 获取id
	 * @return id
	 */
	private synchronized long makeId() {
		return DbUtil.buildId(incrIdGetter.get(), serverGroupId);
	}
}
