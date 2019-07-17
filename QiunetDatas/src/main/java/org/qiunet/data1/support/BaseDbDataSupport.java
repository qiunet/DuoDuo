package org.qiunet.data1.support;

import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntity;

 class BaseDbDataSupport<Po extends IDbEntity, Vo extends IEntityVo<Po>> extends BaseDataSupport<Po, Vo> {
	 protected BaseDbDataSupport(Class<Po> poClass, VoSupplier<Po, Vo> supplier) {
		 super(poClass, supplier);
	 }

	 @Override
	public void syncToDatabase() {
		// 不是异步. 不需要实现
	}

	/***
	 * 更新
	 * @param po
	 * @return
	 */
	public void update(Po po) {
		DefaultDatabaseSupport.getInstance().update(updateStatement, po);
	}

	 @Override
	 public Vo insert(Po po) {
		 DefaultDatabaseSupport.getInstance().insert(insertStatement, po);
		 return supplier.get(po);
	 }

	 /**
	 * 删除
	 * @param po
	 */
	public void delete(Po po) {
		DefaultDatabaseSupport.getInstance().delete(deleteStatement, po);
	}

}
