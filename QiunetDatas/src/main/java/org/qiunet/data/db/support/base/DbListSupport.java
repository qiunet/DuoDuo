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
	public List<PO> selectList(String selectStatment, IEntityListDbInfo dbListObjInfo) {
		return DatabaseSupport.getInstance().selectList(dbListObjInfo.getDbSourceKey(), selectStatment, dbListObjInfo);
	}
}
