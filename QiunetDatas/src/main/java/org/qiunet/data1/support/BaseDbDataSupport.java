package org.qiunet.data1.support;

import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntity;

 class BaseDbDataSupport<Po extends IDbEntity, Bo extends IEntityBo<Po>> extends BaseDataSupport<Po, Bo> {
	 protected BaseDbDataSupport(Class<Po> poClass, BoSupplier<Po, Bo> supplier) {
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
	 public Bo insert(Po po) {
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
