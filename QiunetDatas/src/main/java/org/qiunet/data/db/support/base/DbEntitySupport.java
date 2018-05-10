package org.qiunet.data.db.support.base;

import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.support.info.IEntityDbInfo;

/**
 * @author qiunet
 *         Created on 17/2/9 20:43.
 */
public class DbEntitySupport<PO extends IEntityDbInfo> implements IDbEntity<PO> {

	@Override
	public int insert(PO po, String insertStatment) {
		return DatabaseSupport.getInstance().insert(po.getDbSourceKey(), insertStatment, po);
	}

	@Override
	public void update(PO po, String updateStatment) {
		DatabaseSupport.getInstance().update(po.getDbSourceKey(), updateStatment, po);
	}

	@Override
	public void delete(PO po, String deleteStatment) {
		DatabaseSupport.getInstance().delete(po.getDbSourceKey(), deleteStatment, po);
	}

	@Override
	public PO selectOne(String selectStatment, IEntityDbInfo dbObjInfo) {
		return DatabaseSupport.getInstance().selectOne(dbObjInfo.getDbSourceKey(), selectStatment, dbObjInfo );
	}
}
