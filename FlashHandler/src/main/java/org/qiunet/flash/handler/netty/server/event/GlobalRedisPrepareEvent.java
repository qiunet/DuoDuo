package org.qiunet.flash.handler.netty.server.event;

import org.qiunet.data.redis.IRedisUtil;
import org.qiunet.utils.listener.event.IListenerEvent;

/***
 *
 * @author qiunet
 * 2023/5/10 16:06
 */
public class GlobalRedisPrepareEvent implements IListenerEvent {
	/**
	 * redis
	 */
	private IRedisUtil globalRedis;

	public static GlobalRedisPrepareEvent valueOf(IRedisUtil globalRedis){
		GlobalRedisPrepareEvent data = new GlobalRedisPrepareEvent();
	    data.globalRedis = globalRedis;
		return data;
	}

	public IRedisUtil getGlobalRedis() {
		return globalRedis;
	}
}
