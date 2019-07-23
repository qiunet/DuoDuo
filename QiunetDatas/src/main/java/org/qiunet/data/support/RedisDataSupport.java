package org.qiunet.data.support;

import org.qiunet.data.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.redis.entity.IRedisEntity;
import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.threadLocal.ThreadContextData;

public class RedisDataSupport<Key, Do extends IRedisEntity<Key, Bo>, Bo extends IEntityBo<Do>> extends BaseRedisDataSupport<Do, Bo> {

	public RedisDataSupport(IRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(redisUtil, doClass, supplier);
	}

	/***
	 * 获取到do的json
	 * @param redisKey
	 * @return
	 */
	private Do getDataObjectJson(String redisKey) {
		return redisUtil.execCommands(jedis -> {
			String ret = jedis.get(redisKey);
			if (StringUtil.isEmpty(ret)) return null;

			jedis.expire(redisKey, NORMAL_LIFECYCLE);
			return JsonUtil.getGeneralObject(ret, doClass);
		});
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
	protected void delFromRedis(Do aDo) {
		expire(aDo.key());
	}

	public void expire(Key key) {
		String redisKey = getRedisKey(doName, key);
		returnJedis().expire(redisKey, 0);
		ThreadContextData.removeKey(redisKey);
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

	public Bo getBo(Key key) {
		String redisKey = getRedisKey(doName, key);
		Bo bo = ThreadContextData.get(redisKey);
		if (bo != null) return bo;

		Do aDo = getDataObjectJson(redisKey);
		if (aDo == null) {
			String dbSourceKey = DbUtil.getDbSourceKey(key);
			DbParamMap map = DbParamMap.create().put(defaultDo.keyFieldName(), key)
				.put("dbName", DbUtil.getDbName(key));
			aDo = MoreDbSourceDatabaseSupport.getInstance(dbSourceKey).selectOne(selectStatement, map);
			if (aDo == null) return null;

			bo = supplier.get(aDo);
			this.setDataObjectJson(aDo);
			ThreadContextData.put(redisKey, bo);
		}
		return bo;
	}
}
