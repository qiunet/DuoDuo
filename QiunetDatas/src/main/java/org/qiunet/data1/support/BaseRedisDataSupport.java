package org.qiunet.data1.support;


import org.qiunet.data1.async.SyncType;
import org.qiunet.data1.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data1.core.support.redis.AbstractRedisUtil;
import org.qiunet.data1.redis.entity.IRedisEntity;
import org.qiunet.data1.util.DbProperties;
import redis.clients.jedis.JedisCommands;

import java.util.StringJoiner;

public abstract class BaseRedisDataSupport<Do extends IRedisEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
	/**redis delete 同步队列 key**/
	private String redisDeleteSyncSetKey;
	/**redis insert 同步队列 key**/
	private String redisInsertSyncSetKey;
	/**redis update 同步队列 key**/
	private String redisUpdateSyncSetKey;

	protected boolean async = DbProperties.getInstance().getSyncType() == SyncType.ASYNC;
	private AbstractRedisUtil redisUtil;

	BaseRedisDataSupport(AbstractRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);

		this.redisDeleteSyncSetKey = getRedisKey("DELETE", doClass);
		this.redisInsertSyncSetKey = getRedisKey("INSERT", doClass);
		this.redisUpdateSyncSetKey = getRedisKey("UPDATE", doClass);
	}

	@Override
	public void syncToDatabase() {
		if (!async) return;


	}

	@Override
	public Bo insert(Do aDo) {
		redisUtil.setDataObjectJson(buildRedisDataKey(aDo), aDo);
		if (!async) {
			returnJedis().sadd(redisInsertSyncSetKey, buildSyncKey(aDo));
		}else {
			MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).insert(insertStatement, aDo);
		}
		return supplier.get(aDo);
	}

	@Override
	public void delete(Do aDo) {
		if (!async) {
			this.expireDo(aDo);
			MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).delete(deleteStatement, aDo);
			return;
		}

		String syncKey = buildSyncKey(aDo);
		this.returnJedis().srem(redisUpdateSyncSetKey, syncKey);
		Long insert = this.returnJedis().srem(redisInsertSyncSetKey, syncKey);
		if (insert > 0) {
			// 说明都没有插入数据库
			return;
		}

		this.expireDo(aDo);
		this.returnJedis().sadd(redisDeleteSyncSetKey, syncKey);
	}

	/**
	 * 返回jedis 之后好统一是否需要日志.
	 * @return
	 */
	protected JedisCommands returnJedis(){
		return redisUtil.returnJedisProxy(false);
	}
	@Override
	public void update(Do aDo) {
		redisUtil.setDataObjectJson(buildRedisDataKey(aDo), aDo);
		if (!async) {
			returnJedis().sadd(redisUpdateSyncSetKey, buildSyncKey(aDo));
		} else {
			MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).update(updateStatement, aDo);
		}
	}

	/***
	 * 子类给出sync的key
	 * @param aDo
	 * @return
	 */
	protected abstract String buildSyncKey(Do aDo);
	/***
	 * 子类给出 对象存储在 redis 的key
	 * @param aDo
	 * @return
	 */
	protected abstract String buildRedisDataKey(Do aDo);
	/***
	 * 子类去失效某个do
	 * 因为需要区分 是否是list
	 * @param aDo
	 */
	protected abstract void expireDo(Do aDo);
	/***
	 * 获得redis key
	 * @param keys
	 * @return
	 */
	protected String getRedisKey(Object ... keys) {
		StringJoiner sj = new StringJoiner("#");
		for (Object key : keys) sj.add(String.valueOf(key));
		return sj.toString();
	}
}
