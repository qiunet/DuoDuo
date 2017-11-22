package org.qiunet.flash.handler.netty.server.hook;

/**
 * Created by qiunet.
 * 17/11/22
 */
public interface ShutdownHook {
	/***
	 * shudown 时候做的事情.
	 * 自己实现类.
	 */
	void shutdown();
}
