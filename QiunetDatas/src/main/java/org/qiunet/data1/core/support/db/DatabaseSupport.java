package org.qiunet.data1.core.support.db;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支持db操作
 * @author qiunet
 *         Created on 17/1/23 09:41.
 */
public class DatabaseSupport {
	private volatile static Map<String, DatabaseSupport> instances = new HashMap<>(128);
	private static final DatabaseSupport defaultSupport = new DatabaseSupport(null);
	private DatabaseSupport(String dbSourceKey) {
		this.dbSourceKey = dbSourceKey;
	}
	private static final DbLoader dbLoader = DbLoader.getInstance();
	private String dbSourceKey;
	/**
	 * 根据dbSourceKey 取到执行的DatabaseSupport
	 * @param dbSourceKey
	 * @return
	 */
	public static DatabaseSupport getInstance(String dbSourceKey) {
		return instances.computeIfAbsent(dbSourceKey, DatabaseSupport::new);
	}

	public static DatabaseSupport defaultInstance(){
		return defaultSupport;
	}

	private SqlSession getSqlSession() {
		if (dbSourceKey == null) {
//			map 限制了. key 不可能为null
			return dbLoader.getDefaultSqlSession();
		}

		return dbLoader.getSqlSession(dbSourceKey);
	}
	/**
	 * 删除记录
	 * @param Statement  Statement
	 * @param obj 参数对象
	 * @return 变动数
	 */
	public int delete(String Statement, Object obj) {
		int rt = getSqlSession().delete(Statement, obj);
		return rt;
	}
	/**
	 * 获取记录
	 * @param Statement Statement
	 * @param obj  参数对象
	 * @param <T> 泛型对象
	 * @return 查询的对象
	 */
	public <T> T selectOne(String Statement, Object obj) {
		T rt = getSqlSession().selectOne(Statement, obj);
		return rt;
	}
	/**
	 * 获取记录集合
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @param <T> 泛型对象
	 * @return 查新的list
	 */
	public <T> List<T> selectList(String Statement, Object obj) {
		List<T> rt= getSqlSession().selectList(Statement, obj);
		return rt;
	}
	/**
	 * 插入记录
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @return 变动数
	 */
	@SuppressWarnings("unchecked")
	public int insert(String Statement, Object obj) {
		int rt= getSqlSession().insert(Statement, obj);
		return rt;
	}
	/**
	 * 更新记录
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @return 更新的变动数
	 */
	public int update(String Statement, Object obj) {
		int rt=getSqlSession().update(Statement, obj);
		return rt;
	}
}
