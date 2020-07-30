package org.qiunet.data.core.support.db;

import java.util.List;

public interface IDatabaseSupport {
	/**
	 * 得到该dbSource 对应的dbName
	 * @return
	 */
	String dbName();
	/**
	 * 删除记录
	 * @param Statement  Statement
	 * @param obj 参数对象
	 * @return 变动数
	 */
	 int delete(String Statement, Object obj);
	/**
	 * 获取记录
	 * @param Statement Statement
	 * @param obj  参数对象
	 * @param <T> 泛型对象
	 * @return 查询的对象
	 */
	 <T> T selectOne(String Statement, Object obj);
	/**
	 * 获取记录集合
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @param <T> 泛型对象
	 * @return 查新的list
	 */
	 <T> List<T> selectList(String Statement, Object obj);
	/**
	 * 插入记录
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @return 变动数
	 */
	@SuppressWarnings("unchecked")
	 int insert(String Statement, Object obj);
	/**
	 * 更新记录
	 * @param Statement Statement
	 * @param obj 参数对象
	 * @return 更新的变动数
	 */
	 int update(String Statement, Object obj);
}
