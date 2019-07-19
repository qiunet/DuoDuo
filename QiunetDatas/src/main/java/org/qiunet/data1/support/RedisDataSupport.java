package org.qiunet.data1.support;

import org.qiunet.data1.core.select.DbParamMap;
import org.qiunet.data1.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data1.core.support.redis.AbstractRedisUtil;
import org.qiunet.data1.redis.entity.IRedisEntity;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;
import redis.clients.jedis.Jedis;

public class RedisDataSupport<Key, Do extends IRedisEntity<Key, Bo>, Bo extends IEntityBo<Do>> extends BaseRedisDataSupport<Do, Bo> {

	public RedisDataSupport(AbstractRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(redisUtil, doClass, supplier);
	}

	/***
	 * 获取到do的json
	 * @param key
	 * @return
	 */
	private Do getDataObjectJson(String key) {
		String redisKey = getRedisKey(doName, key);

		try (Jedis jedis = redisUtil.newJedisResource()) {
			String ret = jedis.get(redisKey);
			jedis.expire(redisKey, NORMAL_LIFECYCLE);

			if (StringUtil.isEmpty(ret)) return null;
			return JsonUtil.getGeneralObject(ret, doClass);
		}
	}

	/***
	 * set do的json 到redis
	 * @return
	 */
	private void setDataObjectJson(Do aDo) {
		String redisKey = getRedisKey(doName, aDo.key());

		String json = JsonUtil.toJsonString(aDo);
		returnJedis().setex(redisKey, NORMAL_LIFECYCLE, json);
	}

	@Override
	protected Do getDoBySyncParams(String syncParams) {
		String redisKey = getRedisKey(doName, syncParams);
		return getDataObjectJson(redisKey);
	}

	@Override
	protected void deleteFromDb(Do aDo) {
		DbParamMap map = DbParamMap.create().put(defaultDo.keyFieldName(), aDo.key())
			.put("dbName", aDo.getDbName());
		MoreDbSourceDatabaseSupport.getInstance(aDo.getDbSourceKey()).delete(deleteStatement, map);
	}

	@Override
	protected String buildSyncParams(Do aDo) {
		return String.valueOf(aDo.key());
	}

	@Override
	protected void setDataObjectToRedis(Do aDo) {
		this.setDataObjectJson(aDo);
	}

	@Override
	protected void expireDo(Do aDo) {
		String redisKey = getRedisKey(doName, aDo.key());
		returnJedis().expire(redisKey, 0);
	}

	@Override
	public void delete(Do aDo) {
		super.delete(aDo);

		String redisKey = getRedisKey(doName, aDo.key());
		ThreadContextData.removeKey(redisKey);
	}

	@Override
	public Bo insert(Do aDo) {
		Bo bo = super.insert(aDo);

		String redisKey = getRedisKey(doName, aDo.key());
		ThreadContextData.put(redisKey, bo);
		return bo;
	}
}
