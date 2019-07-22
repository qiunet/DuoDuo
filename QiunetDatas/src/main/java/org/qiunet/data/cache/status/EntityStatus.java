package org.qiunet.data.cache.status;

/***
 * 一个Entity的状态.
 *
 */
public enum  EntityStatus {
	/***
	 * 舒适化状态
	 */
	INIT,
	/***
	 * 正常状态
	 * 除删除外所有操作完成后,
	 * 都恢复这个状态.
	 */
	NORMAL,
	/**
	 * 当前插入状态
	 */
	INSERT,
	/***
	 * 当前更新状态
	 */
	UPDATE,
	/***
	 * 当前待删除状态.
	 */
	DELETE
}
