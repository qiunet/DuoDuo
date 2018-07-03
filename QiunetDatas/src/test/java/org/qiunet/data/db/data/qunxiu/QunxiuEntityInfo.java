package org.qiunet.data.db.data.qunxiu;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.support.info.idInfo.IdEntityDbInfo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.base.RedisDataUtil;
import org.qiunet.data.redis.entity.QunxiuPo;
import org.qiunet.data.redis.key.RedisKey;

/**
 * @author qiunet
 *         Created on 17/2/27 17:31.
 */
public class QunxiuEntityInfo implements IEntityInfo<Integer, QunxiuPo, QunxiuPo> {
	@Override
	public String getNameSpace() {
		return "qunxiu";
	}

	@Override
	public Class<QunxiuPo> getClazz() {
		return QunxiuPo.class;
	}

	@Override
	public boolean needAsync() {
		return true;
	}

	@Override
	public QunxiuPo getVo(QunxiuPo qunxiuPo) {
		return qunxiuPo;
	}

	@Override
	public AbstractRedisUtil getRedisUtil() {
		return RedisDataUtil.getInstance();
	}

	@Override
	public Integer getDbInfoKey(QunxiuPo qunxiuPo) {
		return qunxiuPo.getId();
	}

	@Override
	public String getAsyncKey(Object dbInfoKey) {
		return RedisKey.QUNXIU.getAsyncKey(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(QunxiuPo qunxiuPo) {
		return getEntityDbInfo(getDbInfoKey(qunxiuPo));
	}

	@Override
	public String getRedisKey(Object dbInfoKey) {
		return RedisKey.QUNXIU.getKey(dbInfoKey);
	}

	@Override
	public IEntityDbInfo getEntityDbInfo(Integer dbInfoKey) {
		return new IdEntityDbInfo(dbInfoKey);
	}
}
