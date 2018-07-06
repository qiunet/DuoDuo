package org.qiunet.data.async;

import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiunet
 *         Created on 17/2/21 09:44.
 */
public abstract class BaseAsyncNode implements AsyncNode {
	protected static final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	@Override
	public void updateRedisDataToDatabase() {
		for (int dbIndex : DbProperties.getInstance().getDbIndexList()) {
			String asyncKey = getAsyncKey(dbIndex);
			long size = getRedis().returnJedisProxy().scard(asyncKey);
			for (int i = 0; i < size; i++) {
				String asyncValue = getRedis().returnJedisProxy().spop(asyncKey);
				if (asyncValue == null) break;

				try {
					boolean suc = updateToDb(asyncValue);
					if (!suc) {
						logger.error("Error async update db with string [" + getNodeClassDesc() + "--" + asyncValue + "]");
					}
				} catch (Exception e) {
					getRedis().returnJedisProxy().sadd(asyncKey, asyncValue);
					logger.error("asyncValue [" + asyncValue + "]", e);
				}
			}
		}
	}

	/***
	 * 得到该异步更新节点的名称描述
	 * @return 名称. 是哪个类
	 */
	protected abstract String getNodeClassDesc();
	/**
	 * 得到 asyncKey
	 * @param dbIndex dbIndex
	 * @return key
	 */
	protected abstract String getAsyncKey(int dbIndex);

	/**
	 * 得到redis实例
	 * @return redis
	 */
	protected abstract AbstractRedisUtil getRedis();

	/**
	 *  异步更新
	 * @param asyncValue 异步更新
	 * @return 返回成功与否 成功 true false 的话 asyncValue 会重新加入队列
	 */
	protected abstract boolean updateToDb(String asyncValue) throws  Exception;
}
