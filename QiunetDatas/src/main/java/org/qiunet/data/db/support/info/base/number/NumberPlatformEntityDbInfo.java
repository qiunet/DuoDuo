package org.qiunet.data.db.support.info.base.number;

import org.qiunet.data.db.support.info.IPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/2 15:47
 **/
public abstract class NumberPlatformEntityDbInfo extends NumberEntityDbInfo implements IPlatformEntityDbInfo {
	private PlatformType platformType;

	public NumberPlatformEntityDbInfo(int val, PlatformType platformType) {
		super(val);
		this.platformType = platformType;
	}
	@Override
	public String getPlatformName() {
		return platformType.getName();
	}
}
