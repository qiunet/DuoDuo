package org.qiunet.data.db.support.base;

import org.qiunet.data.db.support.info.IEntityDbInfo;

/**
 * @author qiunet
 *         Created on 17/1/25 12:31.
 */
public interface IDbEntity<PO extends IEntityDbInfo> extends IDbBase<PO> {
	/**
	 * 查询一个数据
	 * @param selectStatement Statement
	 * @param dbObjInfo dbInfo 的对象. 自己封装的也行
	 * @return 返回单个对象
	 */
	public PO selectOne(String selectStatement, IEntityDbInfo dbObjInfo);
}
