package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.IEntityListDbInfo;
import org.qiunet.data.db.util.DbProperties;

/**
 * @author qiunet
 *         Created on 17/2/10 15:30.
 */
public class UidEntityListDbInfo extends UidEntityDbInfo implements IEntityListDbInfo {
	private int subId;
	private int tbIndex;
	public UidEntityListDbInfo(Object uid, int subId) {
		super(uid);
		this.subId = subId;
				
		int tmp = (getUid()/(DbProperties.getInstance().getDbMaxCount()));
		this.tbIndex = tmp % DbProperties.getInstance().getPalyerDataTbDistributeCnt();
	}
	
	@Override
	public int getTbIndex() {
		return tbIndex;
	}
	
	@Override
	public int getSubId() {
		return subId;
	}
}
