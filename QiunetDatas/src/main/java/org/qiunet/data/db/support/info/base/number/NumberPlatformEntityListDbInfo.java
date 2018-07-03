package org.qiunet.data.db.support.info.base.number;

import org.qiunet.data.db.support.info.IPlatformEntityListDbInfo;
import org.qiunet.data.db.util.DbProperties;
import org.qiunet.data.enums.PlatformType;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/2 15:56
 **/
public abstract class NumberPlatformEntityListDbInfo<SubKey> extends NumberPlatformEntityDbInfo implements IPlatformEntityListDbInfo<SubKey> {
	private SubKey subId;
	private int tbIndex;

	public NumberPlatformEntityListDbInfo(int val, PlatformType platformType) {
		this(val, platformType, null);
	}

	public NumberPlatformEntityListDbInfo(int val, PlatformType platformType, SubKey subId) {
		super(val, platformType);
		this.subId = subId;
		this.tbIndex = DbProperties.getInstance().getTbIndexById(val);
	}
	@Override
	public int getTbIndex() {
		return tbIndex;
	}

	@Override
	public SubKey getSubId() {
		return subId;
	}
}
