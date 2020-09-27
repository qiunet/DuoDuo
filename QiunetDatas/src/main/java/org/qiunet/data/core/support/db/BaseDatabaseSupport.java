package org.qiunet.data.core.support.db;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 支持db操作
 * @author qiunet
 *         Created on 17/1/23 09:41.
 */
abstract class BaseDatabaseSupport implements IDatabaseSupport {
	static final DbLoader dbLoader = DbLoader.getInstance();
	abstract SqlSession getSqlSession();
	/**
	 * 删除记录
	 * @param Statement  Statement
	 * @param obj 参数对象
	 * @return 变动数
	 */
	public int delete(String Statement, Object obj) {
		int rt = getSqlSession().delete(Statement, obj);
//		if (rt == 0) {
//			throw new CustomException("测试Redis删除抛异常用, 完事注释");
//		}
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
