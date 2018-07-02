package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.base.number.NumberPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/1/24 16:50.
 */
public class UidPlatformEntityDbInfo extends NumberPlatformEntityDbInfo {
	private int uid;
	public UidPlatformEntityDbInfo(Object uid, PlatformType platformType) {
		super((Integer) uid, platformType);
		this.uid = (int) uid;
	}

	public int getUid() {
		return uid;
	}
}
