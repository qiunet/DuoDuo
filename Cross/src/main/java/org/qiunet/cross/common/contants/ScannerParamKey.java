package org.qiunet.cross.common.contants;

import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.utils.args.ArgumentKey;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 11:41
 */
public interface ScannerParamKey {

	ArgumentKey<IRedisUtil> SERVER_NODE_REDIS_INSTANCE = new ArgumentKey<>();
}
