package org.qiunet.function.id;

import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.core.support.redis.RedisLock;
import org.qiunet.data.redis.util.DbUtil;

import java.util.function.Consumer;
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
	 * 初始ID loader
	 * 一般是从db根据group id 取
	 */
	private final Supplier<Integer> idLoader;
	/**
	 * 保存 到 db
	 */
	private final Consumer<Integer> update;
	/**
	 * 组ID
	 */
	private final int serverGroupId;
	/**
	 * 自增 redis key
	 */
	private final String redisKey;
	/**
	 * 使用到的redis
	 */
	private final IRedisUtil redisUtil;
	/**
	 *
	 * @param redisUtil redis
	 * @param idLoader 初始idLoader 一般是从db根据group id取
	 * @param update 保存到db
	 * @param serverGroupId group id
	 */
	public DistributeIdMaker(String redisKeyPrefix, IRedisUtil redisUtil, Supplier<Integer> idLoader,
							 Consumer<Integer> update, int serverGroupId) {
		this.redisKey = redisKeyPrefix + serverGroupId;
		this.serverGroupId = serverGroupId;
		this.redisUtil = redisUtil;
		this.idLoader = idLoader;
		this.update = update;
	}
	/**
	 * 产生一个ID
	 * @return
	 */
	public long generateId() throws IllegalStateException {
		if (! redisUtil.returnJedis().exists(redisKey)) {
			try (RedisLock redisLock = redisUtil.redisLock(redisKey)) {
				if (redisLock.tryLock()) {
					if (! redisUtil.returnJedis().exists(redisKey)) {
						redisUtil.returnJedis().set(redisKey, String.valueOf(idLoader.get()));
					}
				}else {
					throw new IllegalStateException("Redis lock timeout");
				}
			}
		}
		long incrId = redisUtil.returnJedis().incr(redisKey);
		this.update.accept((int) incrId);
		return DbUtil.buildId(incrId, getServerGroupId());
	}

	/**
	 * 获得对应的server group id
	 * @return
	 */
	public int getServerGroupId() {
		return serverGroupId;
	}

	/**
	 * 获得redis key
	 * 外部可能需要操作什么
	 * @return key
	 */
	public String getRedisKey() {
		return redisKey;
	}
}
