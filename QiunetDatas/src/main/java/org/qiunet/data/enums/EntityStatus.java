package org.qiunet.data.enums;

/***
 * 一个Entity的状态.
 *
 */
public enum  EntityStatus {
	/***
	 * 初始化状态
	 */
	INIT,
	/***
	 * 正常状态
	 * 除删除外所有操作完成后,
	 * 都恢复这个状态.
	 */
	NORMAL,
	/***
	 * 当前更新状态
	 */
	UPDATE,
	/***
	 * 当前待删除状态.
	 */
	DELETE
}
