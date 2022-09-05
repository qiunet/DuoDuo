package org.qiunet.data.support;

import org.qiunet.data.core.select.DbParamMap;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.redis.entity.IRedisEntity;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.thread.ThreadContextData;
import redis.clients.jedis.params.SetParams;

/***
 *
 * @param <Key>
 * @param <Do>
 * @param <Bo>
 * @author qiunet
 *
 */
public final class RedisDataSupport<Key, Do extends IRedisEntity<Key>, Bo extends IEntityBo<Do>> extends BaseRedisDataSupport<Key, Do, Bo> {
	/**防止缓存击穿的 NULL*/
	private final Do NULL;

	private static final String PLACE_HOLDER = "PLACE_HOLDER";

	public RedisDataSupport(IRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(redisUtil, doClass, supplier);
		this.NULL = defaultDo;
	}

	/***
	 * 获取到do的json
	 * @param redisKey
	 * @param defHit 是否需要防止击穿
	 * @return
	 */
	private Do getDataObjectJson(String redisKey, boolean defHit) {
		return redisUtil.execCommands(jedis -> {
			String ret = jedis.get(redisKey);
			if (StringUtil.isEmpty(ret)) {
				return null;
			}

			if (PLACE_HOLDER.equals(ret)) {
				return defHit ? NULL: null;
			}

			jedis.expire(redisKey, NORMAL_LIFECYCLE);
			return JsonUtil.getGeneralObj(ret, doClass);
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
		return getDataObjectJson(redisKey, false);
	}

	@Override
	protected void deleteFromDb(Do aDo) {
		DbParamMap map = DbParamMap.create(table, defaultDo.keyFieldName(), aDo.key());
		databaseSupport().delete(deleteStatement, map);
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
		String redisKey = getRedisKey(doName, aDo.key());
		returnJedis().expire(redisKey, 0);
		ThreadContextData.removeKey(redisKey);
	}

	@Override
	public void delete(Bo bo) {
		super.delete(bo);

		String redisKey = getRedisKey(doName, bo.getDo().key());
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
		if (bo != null) {
			return bo;
		}

		Do aDo = getDataObjectJson(redisKey, true);
		if (aDo == NULL) {
			return null;
		}

		if (aDo == null) {
			DbParamMap map = DbParamMap.create(table, defaultDo.keyFieldName(), key);
			aDo = databaseSupport().selectOne(selectStatement, map);
			if (aDo == null) {
				returnJedis().set(redisKey, PLACE_HOLDER, SetParams.setParams().ex(NORMAL_LIFECYCLE).nx());
				return null;
			}

			bo = supplier.get(aDo);
			this.setDataObjectJson(aDo);
			ThreadContextData.put(redisKey, bo);
		}else{
			bo = supplier.get(aDo);
			ThreadContextData.put(redisKey, bo);
		}
		return bo;
	}
}
