package org.qiunet.data.support;

import org.qiunet.data.db.entity.IDbEntity;

abstract class BaseDbDataSupport<Do extends IDbEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
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
	@Override
	public void update(Do aDo) {
		databaseSupport().update(updateStatement, aDo);
	}

	 @Override
	 public Bo insert(Do aDo) {
		 databaseSupport().insert(insertStatement, aDo);
		 return supplier.get(aDo);
	 }
}
