package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.IPlatformEntityListDbInfo;
import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/2/10 15:31.
 */
public class UidPlatformEntityListDbInfo extends UidPlatformEntityDbInfo implements IPlatformEntityListDbInfo {
	private int subId;
	private int tbIndex;
	
	public UidPlatformEntityListDbInfo(Object uid, PlatformType platformType) {
		this(uid, platformType, 0);
	}
	
	public UidPlatformEntityListDbInfo(Object uid, PlatformType platformType, int subId) {
		super(uid, platformType);
		this.subId = subId;
		this.tbIndex = DbProperties.getInstance().getTbIndexByUid(getUid());
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
