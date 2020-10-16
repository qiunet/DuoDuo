package org.qiunet.cross.common.contants;

import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.utils.args.IArgKey;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 11:41
 */
public final class ScannerParamKey<T> implements IArgKey<T> {

	public static final ScannerParamKey<IRedisUtil> SERVER_NODE_REDIS_INSTANCE = new ScannerParamKey<>();

	private ScannerParamKey(){}
}
