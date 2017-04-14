package org.qiunet.data.redis.support;

import org.qiunet.data.enums.PlatformType;
import org.qiunet.data.redis.support.info.IPlatFormRedisEntity;

/**
 * @author qiunet
 *         Created on 17/2/9 11:14.
 */
public abstract class PlatformRedisEntity extends RedisEntity implements IPlatFormRedisEntity {
	private PlatformType platform;
	@Override
	public void setPlatform(PlatformType platform) {
		this.platform = platform;
	}
	@Override
	public PlatformType getPlatform() {
		return platform;
	}
	@Override
	public String getPlatformName() {
		return platform.getName();
	}
}
