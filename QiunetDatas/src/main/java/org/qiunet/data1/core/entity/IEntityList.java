package org.qiunet.data1.core.entity;

import org.qiunet.data1.support.IEntityVo;

/***
 * 主键对应一条数据的对象.
 * 一 对 一
 * @param <Key> 主键类型
 * @param <SubKey> 联合主键类型
 */
public interface IEntityList<Key, SubKey, Vo extends IEntityVo> extends IEntity<Key, Vo> {
	/***
	 * 得到Entity的联合主键
	 * 一般是物品id 装备id
	 * 或者工会成员id等
	 * @return
	 */
	SubKey subKey();

	/**
	 * 得到联合主键的字段名
	 * @return
	 */
	String subKeyFieldName();
}
