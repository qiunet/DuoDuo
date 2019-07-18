package org.qiunet.data1.support;

import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntity;

 class BaseDbDataSupport<Do extends IDbEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
	 protected BaseDbDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		 super(doClass, supplier);
	 }

	 @Override
	public void syncToDatabase() {
		// 不是异步. 不需要实现
	}

	/***
	 * 更新
	 * @param aDo
	 * @return
	 */
	public void update(Do aDo) {
		DefaultDatabaseSupport.getInstance().update(updateStatement, aDo);
	}

	 @Override
	 public Bo insert(Do aDo) {
		 DefaultDatabaseSupport.getInstance().insert(insertStatement, aDo);
		 return supplier.get(aDo);
	 }

	 /**
	 * 删除
	 * @param aDo
	 */
	public void delete(Do aDo) {
		DefaultDatabaseSupport.getInstance().delete(deleteStatement, aDo);
	}

}
