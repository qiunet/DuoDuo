package org.qiunet.data.redis.support.info;

import org.qiunet.data.core.support.entityInfo.IField;
import org.qiunet.data.db.support.info.IEntityDbInfo;

import java.util.Map;
/**
 *
 * @author qiunet
 *         Created on 16/12/30 07:13.
 */
public interface IRedisEntity extends IEntityDbInfo{
	/***
	 * 返回整个对象对应的map
	 * @return fields 对象的 值的map对象
	 */
	Map<String, String> getAllFeildsToHash();
	/**
	 * 主键
	 * @return dbInfoKey 的名称
	 * */
	String getDbInfoKeyName();

	/***
	 * 返回fields
	 * @return 所有的field名称 除了 dbInfoKey 和subKey
	 */
	IField[] getFields();

	/***
	 * 返回该对象的字段数量
	 * @return Field的数量
	 */
	int getFieldCount();

	/**
	 * 设置一个对象. 作为db操作时候的算法.
	 * @param entityDbInfo 分库方法
	 */
	void setEntityDbInfo(IEntityDbInfo entityDbInfo );
}
