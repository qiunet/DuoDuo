package org.qiunet.data.db.support.base;

import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.redis.support.info.IRedisList;

import java.util.List;

/**
 * @author qiunet
 *         Created on 17/2/8 11:52.
 */
public interface IDbList<PO extends IEntityListDbInfo> extends IDbBase<PO> {
	/**
	 * 获取list
	 * @param selectStatment 查询语句
	 * @param dbListObjInfo 分库使用信息
	 * @return 返回list
	 */
	public List<PO> selectList(String selectStatment, IEntityListDbInfo dbListObjInfo);
}
