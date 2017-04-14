package org.qiunet.data.redis.support.info;

import org.qiunet.data.db.support.info.IPlatformEntityDbInfo;
import org.qiunet.data.enums.PlatformType;

/**
 * 支持平台的对象
 * 只有一个对象
 * @author qiunet
 *         Created on 16/12/29 08:22.
 */
public interface IPlatFormRedisEntity extends IRedisEntity , IPlatformEntityDbInfo{
	
	public void setPlatform(PlatformType platform);
	
	public PlatformType getPlatform();
}
