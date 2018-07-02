package org.qiunet.data.db.support.info.base.number;

import org.qiunet.data.db.support.info.IPlatformEntityListDbInfo;
import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.enums.PlatformType;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/2 15:56
 **/
public abstract class NumberPlatformEntityListDbInfo extends NumberPlatformEntityDbInfo implements IPlatformEntityListDbInfo {
	private int subId;
	private int tbIndex;

	public NumberPlatformEntityListDbInfo(int val, PlatformType platformType) {
		this(val, platformType, 0);
	}

	public NumberPlatformEntityListDbInfo(int val, PlatformType platformType, int subId) {
		super(val, platformType);
		this.subId = subId;
		this.tbIndex = DbProperties.getInstance().getTbIndexById(val);
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
