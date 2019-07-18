package org.qiunet.data1.core.entity;

import org.qiunet.data1.support.IEntityBo;

/***
 * 主键对应一条数据的对象.
 * 一 对 一
 * @param <Key>
 */
public interface IEntity<Key, Bo extends IEntityBo> {
	/***
	 * 得到Entity的主键
	 * 一般是uid  openId
	 * 或者工会id等
	 * @return
	 */
	Key key();

	/***
	 * 主键的字段名
	 * @return
	 */
	String keyFieldName();
	/***
	 * 更新
	 */
	void update();

	/**
	 * 删除
	 */
	void delete();

	/**
	 * 插入
	 */
	Bo insert();
}
