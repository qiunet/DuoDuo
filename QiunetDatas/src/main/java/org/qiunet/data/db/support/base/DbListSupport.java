package org.qiunet.data.db.support.base;

import org.qiunet.data.db.core.DatabaseSupport;
import org.qiunet.data.db.datasource.CustomerContextHolder;
import org.qiunet.data.db.support.info.IEntityListDbInfo;

import java.util.List;

/**
 * @author qiunet
 *         Created on 17/2/10 09:33.
 */
public class DbListSupport<PO extends IEntityListDbInfo> implements IDbList<PO>{
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
	public List<PO> selectList(String selectStatment, IEntityListDbInfo dbListObjInfo) {
		CustomerContextHolder.setCustomerType(dbListObjInfo.getDbSourceType());
		return DatabaseSupport.getInstance().selectList(selectStatment, dbListObjInfo);
	}
}
