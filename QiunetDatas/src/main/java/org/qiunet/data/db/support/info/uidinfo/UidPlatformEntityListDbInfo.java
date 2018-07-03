package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.base.number.NumberPlatformEntityListDbInfo;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/2/10 15:31.
 */
public class UidPlatformEntityListDbInfo<SubKey> extends NumberPlatformEntityListDbInfo<SubKey> {
	private int uid;

	public UidPlatformEntityListDbInfo(int uid, PlatformType platformType) {
		this(uid, platformType, null);
	}

	public UidPlatformEntityListDbInfo(int uid, PlatformType platformType, SubKey subId) {
		super(uid, platformType, subId);
		this.uid = uid;
	}

	public int getUid() {
		return uid;
	}
}
