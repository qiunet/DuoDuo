package org.qiunet.data.model2table.service;

import java.util.List;

public interface ModelService {

	/**
	 * 保存，如果主键有值则进行更新操作
	 * @param <T> model类型
	 * @param t 要保存的model类型数据
	 */
	<T> void insert(T t);

	/**
	 * 执行更新操作
	 * @param <T> model类型
	 * @param t 要保存的model类型数据
	 */
	<T> void update(T t);

	/**
	 * 根据传入对象非空的条件删除
	 * @param <T> model类型
	 * @param t 要删除的model类型数据
	 */
	<T> void delete(T t);

	/**
	 * 根据传入对象非空的条件进行查询
	 * @param <T> model类型
	 * @param t 要查询的model类型数据
	 */
	<T> List<T> query(T t);
}
