package org.qiunet.data.redis.support.info;

import org.qiunet.data.db.support.info.IPlatformEntityListDbInfo;

/**
 * @author qiunet
 *         Created on 17/2/8 15:55.
 */
public interface IPlatformRedisList<SubKey> extends IRedisList<SubKey>, IPlatFormRedisEntity, IPlatformEntityListDbInfo<SubKey> {

}
