package org.qiunet.data.db.support.info.base.number;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.util.DbProperties;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/2 15:32
 **/
public abstract class NumberEntityDbInfo implements IEntityDbInfo {
	private int dbIndex;
	public NumberEntityDbInfo (int val) {
		this.dbIndex = DbProperties.getInstance().getDbIndexById(val);
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
	public String getDbSourceKey() {
		return DbProperties.getInstance().getDataSourceTypeByDbIndex(dbIndex);
	}
}
