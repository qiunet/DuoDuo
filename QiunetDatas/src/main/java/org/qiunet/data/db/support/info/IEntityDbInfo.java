package org.qiunet.data.db.support.info;

/**
 *  如果有特殊的, 可以自己实现该接口.
 * @author qiunet
 *         Created on 17/1/6 09:40.
 */
public interface IEntityDbInfo {
	/***
	 * 得到dbName
	 * @return dbName 在sql表前面的
	 */
	public String getDbName();
	/**
	 * 得到dbIndex
	 * @return
	 */
	public int getDbIndex();
	/**
	 * 得到dbIndex
	 * @return dbSource dbSource路由抉择使用
	 */
	public String getDbSourceType();
}
