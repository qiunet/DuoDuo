package org.qiunet.data.db.support.info.base.string;

import org.qiunet.data.db.support.info.IPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/2 15:59
 **/
public class StringPlatformEntityDbInfo extends StringEntityDbInfo implements IPlatformEntityDbInfo {
	private PlatformType platformType;

	public StringPlatformEntityDbInfo(String val, PlatformType platformType) {
		super(val);
		this.platformType = platformType;
	}
	@Override
	public String getPlatformName() {
		return platformType.getName();
	}
}
