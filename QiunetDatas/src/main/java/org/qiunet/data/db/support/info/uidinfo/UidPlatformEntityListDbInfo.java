package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.base.number.NumberPlatformEntityListDbInfo;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/2/10 15:31.
 */
public class UidPlatformEntityListDbInfo extends NumberPlatformEntityListDbInfo {
	private int uid;

	public UidPlatformEntityListDbInfo(Object uid, PlatformType platformType) {
		this(uid, platformType, 0);
	}

	public UidPlatformEntityListDbInfo(Object uid, PlatformType platformType, int subId) {
		super((Integer) uid, platformType, subId);
		this.uid = (int) uid;
	}

	public int getUid() {
		return uid;
	}
}
