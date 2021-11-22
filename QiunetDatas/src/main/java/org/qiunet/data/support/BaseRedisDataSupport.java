package org.qiunet.data.support;


import org.qiunet.data.core.support.redis.IJedis;
import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.data.redis.entity.IRedisEntity;
import org.qiunet.data.redis.util.DbUtil;
import org.qiunet.data.support.anno.LoadAllData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 *
 * @param <Do>
 * @param <Bo>
 * @author qiunet
 */
public abstract class BaseRedisDataSupport<Do extends IRedisEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
	/***缓存一天*/
	protected static final long NORMAL_LIFECYCLE=86400;

	/**redis update 同步队列 key**/
	private final String redisUpdateSyncSetKey;

	protected IRedisUtil redisUtil;

	BaseRedisDataSupport(IRedisUtil redisUtil, Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
		this.redisUtil = redisUtil;
		this.redisUpdateSyncSetKey = getRedisKey(doName, "SYNC_SET");

		if (doClass.isAnnotationPresent(LoadAllData.class)) {
			List<Do> objects = databaseSupport().selectList(selectAllStatement, null);
			objects.forEach(this::setDataObjectToRedis);
		}
	}

	@Override
	public void syncToDatabase() {
		if (! super.async) {
			return;
		}
		/***
		 * 同步的过程中会出现异常
		 *
		 * 2 . update 出现异常(字段范围不对 超时) , update Redis和玩家不会受影响.
		 *
		 * 所有的操作, 需要先保证redis 的数据是准确的.
		 * 所有的异常必须在24小时内处理, 否则可能出现数据丢失的情况.
		 */
		Set<String> errorSyncParams = new HashSet<>();
		String syncParams;
		while ((syncParams = redisUtil.returnJedis(false).spop(redisUpdateSyncSetKey)) != null) {
			try {
				Do aDo = getDoBySyncParams(syncParams);
				if (aDo == null) {
					logger.error("Do [{}] is not exist, Maybe is expire by somebody!", syncParams);
					continue;
				}
				databaseSupport().update(updateStatement, aDo);
			}catch (Exception e) {
				logger.error("Sync to Database Exception: ", e);
				errorSyncParams.add(syncParams);
			}
		}

		if (!errorSyncParams.isEmpty()) {
			returnJedis().sadd(redisUpdateSyncSetKey, errorSyncParams.toArray(new String[0]));
			errorSyncParams.clear();
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
		databaseSupport().insert(insertStatement, aDo);
		return supplier.get(aDo);
	}

	@Override
	public void delete(Do aDo) {
		this.delFromRedis(aDo);
		this.deleteFromDb(aDo);
	}

	/**
	 * 从数据库删除
	 * @param aDo
	 */
	protected abstract void deleteFromDb(Do aDo);

	/**
	 * 返回jedis 之后好统一是否需要日志.
	 * @return
	 */protected IJedis returnJedis(){
		return redisUtil.returnJedis();
	}

	@Override
	public void update(Do aDo) {
		this.setDataObjectToRedis(aDo);
		if (super.async) {
			redisUtil.returnJedis(false).sadd(redisUpdateSyncSetKey, buildSyncParams(aDo));
		} else {
			databaseSupport().update(updateStatement, aDo);
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
	protected String getRedisKey(String doName, Object ... keys) {
		return DbUtil.buildRedisKey(doName, keys);
	}
}
