package org.qiunet.data.db.core;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 支持db操作
 * @author qiunet
 *         Created on 17/1/23 09:41.
 */
public class DatabaseSupport {
	private volatile static DatabaseSupport instance;
	private final DbLoader dbLoader;
	private DatabaseSupport() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		dbLoader = DbLoader.getInstance();
		instance = this;
	}

	public static DatabaseSupport getInstance() {
		if (instance == null) {
			synchronized (DatabaseSupport.class) {
				if (instance == null)
				{
					new DatabaseSupport();
				}
			}
		}
		return instance;
	}
	private SqlSession getSqlSession(String dbSourceKey) {
		return dbLoader.getSqlSession(dbSourceKey);
	}
	/**
	 * 删除记录
	 * @param Statement  Statement
	 * @param obj 参数对象
	 * @return 变动数
	 */
	public int delete(String dbSourceKey, String Statement, Object obj) {
		int rt = getSqlSession(dbSourceKey).delete(Statement, obj);
		return rt;
	}
	/**
	 * 获取记录
	 * @param Statement Statement
	 * @param obj  参数对象
	 * @param <T> 泛型对象
	 * @return 查询的对象
	 */
	public <T> T selectOne(String dbSourceKey, String Statement, Object obj) {
		T rt = getSqlSession(dbSourceKey).selectOne(Statement, obj);
		return rt;
	}
	/**
	 * 获取记录集合
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @param <T> 泛型对象
	 * @return 查新的list
	 */
	public <T> List<T> selectList(String dbSourceKey, String Statement, Object obj) {
		List<T> rt= getSqlSession(dbSourceKey).selectList(Statement, obj);
		return rt;
	}
	/**
	 * 插入记录
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @return 变动数
	 */
	@SuppressWarnings("unchecked")
	public int insert(String dbSourceKey, String Statement, Object obj) {
		int rt= getSqlSession(dbSourceKey).insert(Statement, obj);
		return rt;
	}
	/**
	 * 更新记录
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @return 更新的变动数
	 */
	public int update(String dbSourceKey, String Statement, Object obj) {
		int rt=getSqlSession(dbSourceKey).update(Statement, obj);
		return rt;
	}
}
