package org.qiunet.data.db.loader;

import org.qiunet.data.cache.status.EntityStatus;

/***
 *  db entity Bo 的接口
 *
 * @author qiunet
 * 2021/11/22 18:15
 */
public interface IDbEntityBo {
	/**
	 * 更新状态
	 * @param status
	 */
	void updateEntityStatus(EntityStatus status);
}
