package org.qiunet.data.core.support.db;

import org.apache.ibatis.session.SqlSession;

/***
 * 单个数据库源的方式
 */
public final class DefaultDatabaseSupport extends BaseDatabaseSupport {
	private static final IDatabaseSupport instance = new DefaultDatabaseSupport();
	private DefaultDatabaseSupport() {
		if (instance != null) {
			throw new RuntimeException("Instance Duplication!");
		}
	}
	/**
	 * 根据dbSourceKey 取到执行的DatabaseSupport
	 * @return
	 */
	public static IDatabaseSupport getInstance() {
		return instance;
	}

	public String getDbName(){
		return dbLoader.getDefaultDbName();
	}

	@Override
	SqlSession getSqlSession() {
		return dbLoader.getDefaultSqlSession();
	}
}
