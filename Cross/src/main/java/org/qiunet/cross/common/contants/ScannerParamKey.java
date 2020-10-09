package org.qiunet.cross.common.contants;

import org.qiunet.data.core.support.redis.IRedisUtil;
import org.qiunet.utils.args.AbstractArgKey;

/***
 *
 *
 * @author qiunet
 * 2020-10-09 11:41
 */
public final class ScannerParamKey<T> extends AbstractArgKey<T> {

	public static final ScannerParamKey<IRedisUtil> SERVER_NODE_REDIS_INSTANCE = new ScannerParamKey<>("REDIS_INSTANCE");

	private ScannerParamKey(String name) {
		super(name);
	}

}
