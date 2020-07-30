package org.qiunet.data.core.support.db;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/***
 * 多个数据库源的方式
 */
public final class DbSourceDatabaseSupport extends BaseDatabaseSupport {
	private volatile static Map<String, IDatabaseSupport> instances = new HashMap<>(128);
	private DbSourceDatabaseSupport(String dbSourceName) {
		this.dbSourceName = dbSourceName;
	}
	private String dbSourceName;

	/**
	 * 根据dbSourceKey 取到执行的DatabaseSupport
	 * @param dbSourceName
	 * @return
	 */
	public static IDatabaseSupport getInstance(String dbSourceName) {
		if (! dbLoader.contains(dbSourceName)) {
			throw new IllegalArgumentException("Not have db source for name ["+dbSourceName+"]");
		}
		return instances.computeIfAbsent(dbSourceName, DbSourceDatabaseSupport::new);
	}

	@Override
	public String dbName() {
		return dbLoader.dbName(dbSourceName);
	}

	@Override
	SqlSession getSqlSession() {
		return dbLoader.getSqlSession(dbSourceName);
	}
}
