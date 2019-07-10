package org.qiunet.data1.support;

import org.qiunet.data1.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data1.db.entity.IDbEntity;

 class BaseDbDataSupport<Po extends IDbEntity> extends BaseDataSupport<Po> {
	@Override
	public void syncToDatabase() {
		// 不是异步. 不需要实现
	}

	/**
	 * 插入
	 * @param po
	 * @return
	 */
	public int insert(Po po) {
		return DefaultDatabaseSupport.getInstance().insert(insertStatement, po);
	}

	/***
	 * 更新
	 * @param po
	 * @return
	 */
	public int update(Po po) {
		return DefaultDatabaseSupport.getInstance().update(updateStatement, po);
	}

	/**
	 * 删除
	 * @param po
	 */
	public int delete(Po po) {
		return DefaultDatabaseSupport.getInstance().delete(deleteStatement, po);
	}

}
