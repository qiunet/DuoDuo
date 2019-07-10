package org.qiunet.data1.cache.entity;

import org.qiunet.data1.cache.status.EntityStatus;
import org.qiunet.data1.db.entity.IDbEntity;

/***
 * 默认使用Cache
 * @param <Key>
 */
public interface ICacheEntity<Key> extends IDbEntity<Key> {
	/**
	 * 原子性更新状态
	 * @param status
	 */
	boolean atomicSetEntityStatus(EntityStatus status);
	/**
	 * 必须符合预期, 才会插入.
	 * 比如执行INSERT后, 必须是INSERT状态改回NORMAL
	 * @param expect
	 * @param status
	 * @return
	 */
	boolean atomicSetEntityStatus(EntityStatus expect, EntityStatus status);
	/**
	 * 得到当前的对象状态
	 * @return
	 */
	EntityStatus entityStatus();
}
