package org.qiunet.data1.core.support.db;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/***
 * 多个数据库源的方式
 */
public final class MoreDbSourceDatabaseSupport extends BaseDatabaseSupport {
	private volatile static Map<String, IDatabaseSupport> instances = new HashMap<>(128);
	private MoreDbSourceDatabaseSupport(String dbSourceKey) {
		this.dbSourceKey = dbSourceKey;
	}
	private String dbSourceKey;

	/**
	 * 根据dbSourceKey 取到执行的DatabaseSupport
	 * @param dbSourceKey
	 * @return
	 */
	public static IDatabaseSupport getInstance(String dbSourceKey) {
		return instances.computeIfAbsent(dbSourceKey, MoreDbSourceDatabaseSupport::new);
	}

	@Override
	SqlSession getSqlSession() {
		return dbLoader.getSqlSession(dbSourceKey);
	}
}
