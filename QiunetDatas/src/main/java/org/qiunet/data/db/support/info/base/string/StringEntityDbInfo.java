package org.qiunet.data.db.support.info.base.string;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.util.DbProperties;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/2 15:14
 **/
public abstract class StringEntityDbInfo implements IEntityDbInfo {
	private int dbIndex;

	public StringEntityDbInfo(String val) {
		this.dbIndex = Math.abs(val.hashCode() % DbProperties.getInstance().getLoginNeedDb());
	}

	@Override
	public String getDbName() {
		return DbProperties.getInstance().getDbNameByDbIndex(dbIndex);
	}
	@Override
	public int getDbIndex() {
		return dbIndex;
	}
	@Override
	public String getDbSourceKey() {
		return DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);
	}
}
