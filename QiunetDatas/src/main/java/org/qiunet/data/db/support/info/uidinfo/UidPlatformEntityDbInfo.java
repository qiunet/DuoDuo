package org.qiunet.data.db.support.info.uidinfo;

import org.qiunet.data.db.support.info.IPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;

/**
 * @author qiunet
 *         Created on 17/1/24 16:50.
 */
public class UidPlatformEntityDbInfo extends UidEntityDbInfo implements IPlatformEntityDbInfo{
	private PlatformType platformType;
	
	public UidPlatformEntityDbInfo(Object uid, PlatformType platformType) {
		super(uid);
		this.platformType = platformType;
	}
	@Override
	public String getPlatformName() {
		return platformType.getName();
	}
}
