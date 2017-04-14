package org.qiunet.data.core.support.entityInfo;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.redis.AbstractRedisUtil;
import org.qiunet.data.redis.support.info.IRedisEntity;

/**
 * @author qiunet
 *         Created on 17/2/13 14:37.
 */
public interface IBaseEntityInfo<PO extends IRedisEntity, VO> {
	/**
	 * 得到数据库使用的nameSpace
	 * @return 数据库batis nameSpase
	 */
	public String getNameSpace();
	/**
	 * 得到po的class
	 * @return po 的class
	 */
	public Class<PO> getClazz();
	/**
	 * 是否需要异步更新
	 * @return 是否异步更新
	 */
	public boolean needAsync();
	/**
	 * po 转 vo
	 * @param po 需要转vo的po
	 * @return vo
	 */
	public VO getVo(PO po);
	/***
	 * 得到redis的实例
	 * @return  AbstractRedisUtil子类实例
	 */
	public AbstractRedisUtil getRedisUtil();
	/**
	 * 得到分库用的 id
	 * @param po po对象
	 * @return 返回的dbINfoKey
	 */
	public Object getDbInfoKey(PO po);
	/***
	 * 得到异步更新数据库的key key+dbIndex
	 * 存redis队列的key 名称
	 * @param dbInfoKey 分库使用的key 一般uid
	 * @return 异步更新的key  当needAsync为true 有效
	 */
	public String getAsyncKey(Object dbInfoKey);
	/***
	 * 返回分库的信息
	 * @param po po对象
	 * @return 返回dbINfo对象.
	 */
	public IEntityDbInfo getEntityDbInfo(PO po);
}
