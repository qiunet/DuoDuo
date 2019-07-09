package org.qiunet.data1.core.entity;

/***
 * 主键对应一条数据的对象.
 * 一 对 一
 * @param <Key>
 */
public interface IEntity<Key> {
	/***
	 * 得到Entity的主键
	 * 一般是uid  openId
	 * 或者工会id等
	 * @return
	 */
	Key getKey();
}
