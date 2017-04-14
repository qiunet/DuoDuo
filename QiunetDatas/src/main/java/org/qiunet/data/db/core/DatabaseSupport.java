package org.qiunet.data.db.core;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import java.util.List;

/**
 * 支持db操作
 * @author qiunet
 *         Created on 17/1/23 09:41.
 */
public class DatabaseSupport extends SqlSessionDaoSupport {
	private volatile static DatabaseSupport instance;
	
	private DatabaseSupport() {
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
	/**
	 * 删除记录
	 * @param statment  statment
	 * @param obj 参数对象
	 * @return 变动数
	 */
	public int delete(String statment, Object obj) {
		int rt = getSqlSession().delete(statment, obj);
		return rt;
	}
	/**
	 * 获取记录
	 * @param statment statment
	 * @param obj  参数对象
	 * @param <T> 泛型对象               
	 * @return 查询的对象
	 */
	public <T> T selectOne(String statment, Object obj) {
		T rt = getSqlSession().selectOne(statment, obj);
		return rt;
	}
	/**
	 * 获取记录集合
	 * @param statment statment
	 * @param obj 参数对象
	 * @param <T> 泛型对象   
	 * @return 查新的list
	 */
	public <T> List<T> selectList(String statment, Object obj) {
		List<T> rt= getSqlSession().selectList(statment, obj);
		return rt;
	}
	/**
	 * 插入记录
	 * @param statment statment
	 * @param obj 参数对象
	 * @return 变动数
	 */
	@SuppressWarnings("unchecked")
	public int insert(String statment, Object obj) {
		int rt= getSqlSession().insert(statment, obj);
		return rt;
	}
	/**
	 * 更新记录
	 * @param statment statment
	 * @param obj 参数对象
	 * @return 更新的变动数
	 */
	public int update(String statment, Object obj) {
		int rt=getSqlSession().update(statment, obj);
		return rt;
	}
}
