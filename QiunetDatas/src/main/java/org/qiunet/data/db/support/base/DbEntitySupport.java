package org.qiunet.data.db.support.base;

import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.support.info.IEntityDbInfo;

/**
 * @author qiunet
 *         Created on 17/2/9 20:43.
 */
public class DbEntitySupport<PO extends IEntityDbInfo> implements IDbEntity<PO> {

	@Override
	public int insert(PO po, String insertStatement) {
		return DatabaseSupport.getInstance().insert(po.getDbSourceKey(), insertStatement, po);
	}

	@Override
	public void update(PO po, String updateStatement) {
		DatabaseSupport.getInstance().update(po.getDbSourceKey(), updateStatement, po);
	}

	@Override
	public void delete(PO po, String deleteStatement) {
		DatabaseSupport.getInstance().delete(po.getDbSourceKey(), deleteStatement, po);
	}

	@Override
	public PO selectOne(String selectStatement, IEntityDbInfo dbObjInfo) {
		return DatabaseSupport.getInstance().selectOne(dbObjInfo.getDbSourceKey(), selectStatement, dbObjInfo );
	}
}
