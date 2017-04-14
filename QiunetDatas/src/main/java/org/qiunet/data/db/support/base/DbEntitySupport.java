package org.qiunet.data.db.support.base;

import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.datasource.CustomerContextHolder;
import org.qiunet.data.db.support.base.IDbEntity;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.redis.support.info.IRedisEntity;

/**
 * @author qiunet
 *         Created on 17/2/9 20:43.
 */
public class DbEntitySupport<PO extends IEntityDbInfo> implements IDbEntity<PO> {
	
	@Override
	public int insert(PO po, String insertStatment) {
		CustomerContextHolder.setCustomerType(po.getDbSourceType());
		return DatabaseSupport.getInstance().insert(insertStatment, po);
	}
	
	@Override
	public void update(PO po, String updateStatment) {
		CustomerContextHolder.setCustomerType(po.getDbSourceType());
		DatabaseSupport.getInstance().update(updateStatment, po);
	}
	
	@Override
	public void delete(PO po, String deleteStatment) {
		CustomerContextHolder.setCustomerType(po.getDbSourceType());
		DatabaseSupport.getInstance().delete(deleteStatment, po);
	}
	
	@Override
	public PO selectOne(String selectStatment, IEntityDbInfo dbObjInfo) {
		CustomerContextHolder.setCustomerType(dbObjInfo.getDbSourceType());
		return DatabaseSupport.getInstance().selectOne(selectStatment, dbObjInfo );
	}
}
