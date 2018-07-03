package org.qiunet.data.core.support;

import org.qiunet.data.async.AsyncJobSupport;
import org.qiunet.data.async.BaseAsyncNode;
import org.qiunet.data.core.support.entityInfo.IBaseEntityInfo;
import org.qiunet.data.db.support.base.IDbBase;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.support.info.IRedisEntity;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiunet
 *         Created on 17/2/10 18:18.
 */
abstract class BaseDataSupport<PO extends IRedisEntity>  extends BaseAsyncNode {
	protected final Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);

	/*** db的使用 */
	protected IDbBase<PO> dbSupport;

	protected String insertStatment;
	protected String updateStatment;
	protected String deleteStatment;
	protected String selectStatment;

	private IBaseEntityInfo entityInfo;

	protected BaseDataSupport(IDbBase<PO> dbSupport, IBaseEntityInfo entityInfo){
		this.dbSupport = dbSupport;
		this.entityInfo = entityInfo;
		/***
		 * 添加到异步更新里面
		 */
		if (entityInfo.needAsync()) {
			AsyncJobSupport.getInstance().addNode(this);
		}

		this.insertStatment = entityInfo.getNameSpace()+".insert"+entityInfo.getClazz().getSimpleName();
		this.updateStatment = entityInfo.getNameSpace()+".update"+entityInfo.getClazz().getSimpleName();
		this.deleteStatment = entityInfo.getNameSpace()+".delete"+entityInfo.getClazz().getSimpleName();
	}

	@Override
	protected String getAsyncKey(int dbIndex) {
		return entityInfo.getAsyncKey(dbIndex);
	}

	@Override
	protected String getNodeClassDesc() {
		return entityInfo.getClazz().getSimpleName();
	}

	@Override
	protected AbstractRedisUtil getRedis() {
		return entityInfo.getRedisUtil();
	}
}
