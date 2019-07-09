package org.qiunet.data1.core.support.db;

import org.apache.ibatis.session.SqlSession;

/***
 * 单个数据库源的方式
 */
public final class DefaultDatabaseSupport extends BaseDatabaseSupport {
	private static final DefaultDatabaseSupport instance = new DefaultDatabaseSupport();
	private DefaultDatabaseSupport() {
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}
	}
	/**
	 * 根据dbSourceKey 取到执行的DatabaseSupport
	 * @return
	 */
	public static BaseDatabaseSupport getInstance() {
		return instance;
	}

	@Override
	SqlSession getSqlSession() {
		return dbLoader.getDefaultSqlSession();
	}
}
