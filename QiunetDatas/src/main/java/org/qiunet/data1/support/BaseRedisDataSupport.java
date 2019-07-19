package org.qiunet.data1.support;


import org.qiunet.data1.async.SyncType;
import org.qiunet.data1.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data1.core.support.redis.AbstractRedisUtil;
import org.qiunet.data1.redis.entity.IRedisEntity;
import org.qiunet.data1.util.DbProperties;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import redis.clients.jedis.JedisCommands;

import java.util.StringJoiner;

public abstract class BaseRedisDataSupport<Do extends IRedisEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
	/***缓存一天*/
	protected final int NORMAL_LIFECYCLE=86400;

	private enum RedisSyncSetKey {
		DELETE, INSERT, UPDATE;
		public String getSyncSetKeyName(String doName){
			return name() + "#SYNC_SET#" + doName;
		}
		private static RedisSyncSetKey [] values = values();
	}
	/**redis delete 同步队列 key**/
	private String redisDeleteSyncSetKey;
	/**redis insert 同步队列 key**/
	private String redisInsertSyncSetKey;
	/**redis update 同步队列 key**/
	private String redisUpdateSyncSetKey;

	protected boolean async = DbProperties.getInstance().getSyncType() == SyncType.ASYNC;
	protected AbstractRedisUtil redisUtil;

	BaseRedisDataSupport(AbstractRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
		this.redisUtil = redisUtil;
		this.redisDeleteSyncSetKey = RedisSyncSetKey.DELETE.getSyncSetKeyName(doName);
		this.redisInsertSyncSetKey = RedisSyncSetKey.INSERT.getSyncSetKeyName(doName);
		this.redisUpdateSyncSetKey = RedisSyncSetKey.UPDATE.getSyncSetKeyName(doName);
	}

	@Override
	public void syncToDatabase() {
		if (!async) return;

		for (RedisSyncSetKey syncSetKey : RedisSyncSetKey.values) {
			String syncSetKeyName = syncSetKey.getSyncSetKeyName(doName);
			String syncParams;
			while ((syncParams = returnJedis().spop(syncSetKeyName)) != null) {
				try {
					switch (syncSetKey) {
						case UPDATE:
							Do aDo = getDoBySyncParams(syncParams);
							MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).update(updateStatement, aDo);
							break;
						case DELETE:
							String deleteRedisKey = buildDeleteRedisKey(syncParams);
							aDo = returnDataObjByRedisKey(deleteRedisKey);
							this.deleteFromDb(aDo);
							// 不会读到deleted key 可以不删除. 万一有异常. 还能往回get出来.
							// returnJedis().del(deleteRedisKey);
							break;
						case INSERT:
							aDo = getDoBySyncParams(syncParams);
							MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).insert(insertStatement, aDo);
							break;
					}
				}catch (Exception e) {
					logger.error("Sync to Database Exception: ", e);
					returnJedis().sadd(syncSetKeyName, syncParams);
				}
			}
		}
	}

	/**
	 * 由syncParams 得到 Do 子类实现
	 * @param syncParams
	 * @return
	 */
	protected abstract Do getDoBySyncParams(String syncParams);

	@Override
	public Bo insert(Do aDo) {
		this.setDataObjectToRedis(aDo);
		if (async) {
			returnJedis().sadd(redisInsertSyncSetKey, buildSyncParams(aDo));
		}else {
			MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).insert(insertStatement, aDo);
		}
		return supplier.get(aDo);
	}

	@Override
	public void delete(Do aDo) {
		if (!async) {
			this.expireDo(aDo);
			this.deleteFromDb(aDo);
			return;
		}

		String syncKey = buildSyncParams(aDo);
		this.returnJedis().srem(redisUpdateSyncSetKey, syncKey);
		Long insert = this.returnJedis().srem(redisInsertSyncSetKey, syncKey);
		if (insert > 0) {
			// 说明都没有插入数据库
			return;
		}

		this.expireDo(aDo);
		this.sendDataObjToRedis(buildDeleteRedisKey(buildSyncParams(aDo)), aDo);
		this.returnJedis().sadd(redisDeleteSyncSetKey, syncKey);
	}

	/***
	 * 删除的key 保存在其它地方.
	 * @param syncParams
	 * @return
	 */
	private String buildDeleteRedisKey(String syncParams){
		return getRedisKey(doName, "DELETED", syncParams);
	}
	/**
	 * 从数据库删除
	 * @param aDo
	 */
	protected abstract void deleteFromDb(Do aDo);

	/**
	 * 返回jedis 之后好统一是否需要日志.
	 * @return
	 */protected JedisCommands returnJedis(){
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
