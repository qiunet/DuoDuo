package org.qiunet.data.support;


import org.qiunet.data.async.SyncType;
import org.qiunet.data.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.redis.entity.IRedisEntity;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.JedisCommands;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public abstract class BaseRedisDataSupport<Do extends IRedisEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
	/***缓存一天*/
	protected final int NORMAL_LIFECYCLE = 86400;

	private enum RedisSyncSetKey {
		DELETE, INSERT, UPDATE;

		public String getSyncSetKeyName(String doName) {
			return name() + "#SYNC_SET#" + doName;
		}

		private static RedisSyncSetKey[] values = values();
	}

	/**
	 * redis delete 同步队列 key
	 **/
	private String redisDeleteSyncSetKey;
	/**
	 * redis insert 同步队列 key
	 **/
	private String redisInsertSyncSetKey;
	/**
	 * redis update 同步队列 key
	 **/
	private String redisUpdateSyncSetKey;

	protected boolean async = ServerConfig.instance.getSyncType() == SyncType.ASYNC;
	protected IRedisUtil redisUtil;

	BaseRedisDataSupport(IRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
		this.redisUtil = redisUtil;
		this.redisDeleteSyncSetKey = RedisSyncSetKey.DELETE.getSyncSetKeyName(doName);
		this.redisInsertSyncSetKey = RedisSyncSetKey.INSERT.getSyncSetKeyName(doName);
		this.redisUpdateSyncSetKey = RedisSyncSetKey.UPDATE.getSyncSetKeyName(doName);
	}

	@Override
	public void syncToDatabase() {
		if (!async) return;
		/***
		 * 同步的过程中会出现异常
		 *
		 * 1 . insert 出现异常(duplicate 字段范围 超时), update 不会守影响. insert 会每次同步都进行. 直到出现delete, 会清除insert 的同步状态.
		 * 2 . update 出现异常(字段范围不对 超时) , update Redis和玩家不会受影响. 直到出现delete 会清除update状态.
		 * 3 . delete 出现异常(超时), 会一直尝试delete, 直到下次insert 时候, 会尝试直接delete一次, 然后清除状态.
		 *
		 * 所有的操作, 需要先保证redis 的数据是准确的.
		 * 所有的异常必须在24小时内处理, 否则可能出现数据丢失的情况.
		 */
		Set<String> errorSyncParams = new HashSet<>();
		for (RedisSyncSetKey syncSetKey : RedisSyncSetKey.values) {
			String syncSetKeyName = syncSetKey.getSyncSetKeyName(doName);
			String syncParams;
			while ((syncParams = returnJedis().spop(syncSetKeyName)) != null) {
				try {
					switch (syncSetKey) {
						case UPDATE:
							Do aDo = getDoBySyncParams(syncParams);
							if (aDo == null) {
								logger.error("Do [" + syncParams + "] is not exist, Maybe is expire by somebody!");
								continue;
							}
							MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).update(updateStatement, aDo);
							break;
						case DELETE:
							this.deleteBySyncParams(syncParams);
							break;
						case INSERT:
							aDo = getDoBySyncParams(syncParams);
							if (aDo == null) {
								logger.error("Do [" + syncParams + "] is not exist, Maybe is expire by somebody!");
								continue;
							}
							MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).insert(insertStatement, aDo);
							break;
					}
				} catch (Exception e) {
					logger.error("Sync to Database Exception: ", e);
					errorSyncParams.add(syncParams);
				}
			}

			if (!errorSyncParams.isEmpty()) {
				returnJedis().sadd(syncSetKeyName, errorSyncParams.toArray(new String[0]));
				errorSyncParams.clear();
			}
		}
	}

	private void deleteBySyncParams(String syncParams) {
		String deleteRedisKey = buildDeleteRedisKey(syncParams);
		Do aDo = returnDataObjByRedisKey(deleteRedisKey);
		this.deleteFromDb(aDo);
		// 不会读到deleted key 可以不删除. 万一有异常. 还能往回get出来.
		// returnJedis().del(deleteRedisKey);
	}

	/**
	 * 由syncParams 得到 Do 子类实现
	 *
	 * @param syncParams
	 * @return
	 */
	protected abstract Do getDoBySyncParams(String syncParams);

	@Override
	public Bo insert(Do aDo) {
		boolean asyncOpen = false;    //先屏蔽掉异步开关
		this.setDataObjectToRedis(aDo);
		if (asyncOpen && async) {
			String syncParams = buildSyncParams(aDo);
			Long rem = returnJedis().srem(redisDeleteSyncSetKey, syncParams);
			if (rem != null && rem > 0) {
				try {
					// insert 前先执行之前的delete操作.
					this.deleteBySyncParams(syncParams);
				} catch (Exception e) {
					logger.error("Delete Exception", e);
				}
			}

			returnJedis().sadd(redisInsertSyncSetKey, syncParams);
		} else {
			MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).insert(insertStatement, aDo);
		}
		return supplier.get(aDo);
	}

	@Override
	public void delete(Do aDo) {
		boolean asyncOpen = false;    //先屏蔽掉异步开关
		if (asyncOpen && async) {
			String syncKey = buildSyncParams(aDo);
			this.returnJedis().srem(redisUpdateSyncSetKey, syncKey);
			this.returnJedis().srem(redisInsertSyncSetKey, syncKey);
//			if (insert > 0) {
//				// 说明都没有插入数据库
//				 但是前面有insert失败的情况. delete 会使情况恢复正常
//				return;
//			}
			this.delFromRedis(aDo);
			this.sendDataObjToRedis(buildDeleteRedisKey(buildSyncParams(aDo)), aDo);
			this.returnJedis().sadd(redisDeleteSyncSetKey, syncKey);
		} else {
			this.delFromRedis(aDo);
			this.deleteFromDb(aDo);
		}
	}

	/***
	 * 删除的key 保存在其它地方.
	 * @param syncParams
	 * @return
	 */
	private String buildDeleteRedisKey(String syncParams) {
		return getRedisKey(doName, "DELETED", syncParams);
	}

	/**
	 * 从数据库删除
	 *
	 * @param aDo
	 */
	protected abstract void deleteFromDb(Do aDo);

	/**
	 * 返回jedis 之后好统一是否需要日志.
	 *
	 * @return
	 */
	protected JedisCommands returnJedis() {
		return redisUtil.returnJedis();
	}

	@Override
	public void update(Do aDo) {
		this.setDataObjectToRedis(aDo);
		if (async) {
			returnJedis().sadd(redisUpdateSyncSetKey, buildSyncParams(aDo));
		} else {
			MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).update(updateStatement, aDo);
		}
	}

	/***
	 * 子类给出sync的参数
	 * @param aDo
	 * @return
	 */
	protected abstract String buildSyncParams(Do aDo);

	/***
	 * set Object to redis
	 * @param aDo
	 * @return
	 */
	protected abstract void setDataObjectToRedis(Do aDo);

	/***
	 * 子类去失效某个do
	 * 因为需要区分 是否是list
	 * @param aDo
	 */
	protected abstract void delFromRedis(Do aDo);

	/***
	 * 获得redis key
	 * @param keys
	 * @return
	 */
	protected String getRedisKey(Object... keys) {
		StringJoiner sj = new StringJoiner("#");
		for (Object key : keys) sj.add(String.valueOf(key));
		return sj.toString();
	}

	private void sendDataObjToRedis(String redisKey, Do aDo) {
		redisUtil.execCommands(jedis -> {
			jedis.set(redisKey, JsonUtil.toJsonString(aDo));
			jedis.expire(redisKey, NORMAL_LIFECYCLE);
			return null;
		});
	}

	private Do returnDataObjByRedisKey(String redisKey) {
		return redisUtil.execCommands(jedis -> {
			String json = jedis.get(redisKey);
			if (StringUtil.isEmpty(json)) return null;

			return JsonUtil.getGeneralObject(json, doClass);
		});
	}
}
