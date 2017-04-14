package org.qiunet.data.db.support.base;

import org.qiunet.data.db.support.info.IEntityDbInfo;

/**
 * 通用的db接口
 * @author qiunet
 *         Created on 17/1/25 12:11.
 */
public interface IDbBase<Po extends IEntityDbInfo> {
	/***
	 * 插入
	 * @param po 插入的po
	 * @param insertStatment statment
	 * @return insert导致变动的数
	 */
	public int insert(Po po, String insertStatment);
	
	/***
	 * 更新
	 * @param po 更新的po
	 * @param updateStatment statment
	 */
	public void update(Po po, String updateStatment);
	
	/**
	 * 删除
	 * @param po 删除的po
	 * @param deleteStatment statment
	 */ 
	public void delete(Po po, String deleteStatment);
}
