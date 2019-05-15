package org.qiunet.data.db.support.base;

import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.support.info.IEntityListDbInfo;

import java.util.List;

/**
 * @author qiunet
 *         Created on 17/2/10 09:33.
 */
public class DbListSupport<PO extends IEntityListDbInfo> implements IDbList<PO>{
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
	public List<PO> selectList(String selectStatement, IEntityListDbInfo dbListObjInfo) {
		return DatabaseSupport.getInstance().selectList(dbListObjInfo.getDbSourceKey(), selectStatement, dbListObjInfo);
	}
}
