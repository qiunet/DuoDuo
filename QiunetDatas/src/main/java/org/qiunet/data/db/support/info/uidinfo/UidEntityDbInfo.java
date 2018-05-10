package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.util.DbProperties;

import java.util.HashMap;

/**
 * @author qiunet
 *         Created on 17/1/24 16:10.
 */
public class UidEntityDbInfo implements IEntityDbInfo {
	private int uid;
	private int dbIndex;
	public UidEntityDbInfo( Object uid) {
		this.uid = (Integer)uid;
		this.dbIndex = DbProperties.getInstance().getDbIndexByUid(getUid());
	}
	public int getUid(){
		return uid;
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
