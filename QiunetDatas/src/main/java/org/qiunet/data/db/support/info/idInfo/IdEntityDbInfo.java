package org.qiunet.data.db.support.info.idInfo;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.util.DbProperties;

/**
 * 一般id 不包含uid等, 所以利用id分库
 * @author qiunet
 *         Created on 17/2/24 18:22.
 */
public class IdEntityDbInfo implements IEntityDbInfo{
	private int id;
	private int dbIndex;
	public IdEntityDbInfo (Object id) {
		this.id = (Integer) id;
		this.dbIndex = (this.id % DbProperties.getInstance().getDbMaxCount());
	}

	@Override
	public String getDbName() {
		return DbProperties.getInstance().getDbNamePrefix() + dbIndex;
	}

	@Override
	public int getDbIndex() {
		return dbIndex;
	}

	@Override
	public String getDbSourceType() {
		return DbProperties.getInstance().getDataSourceTypeByDbIndex(dbIndex);
	}
}
