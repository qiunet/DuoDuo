package org.qiunet.data1.support;

import org.qiunet.data1.db.entity.IDbEntityList;

public class DbDataListSupport<Po extends IDbEntityList> extends BaseDbDataSupport<Po> {

	@Override
	public void syncToDatabase() {
		// 没有异步. 不需要做什么
	}
}
