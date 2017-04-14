package org.qiunet.data.db.support.info.openidInfo;

import org.qiunet.data.core.support.entityInfo.IEntityInfo;
import org.qiunet.data.db.support.info.IEntityDbInfo;
import org.qiunet.data.db.util.DbProperties;

/**
 * @author qiunet
 *         Created on 17/2/13 11:48.
 */
public class OpenidEntityDbInfo implements IEntityDbInfo {
	private String openid;
	private int dbIndex;
	public OpenidEntityDbInfo(Object openid) {
		this.openid = (String)openid;
		this.dbIndex = Math.abs(openid.hashCode()) % DbProperties.getInstance().getDbMaxCount();
	}
	public String getOpenid() {
		return openid;
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
	public String getDbSourceType() {
		return DbProperties.getInstance().getDataSourceTypeByDbIndex(dbIndex);
	}
}
