package org.qiunet.flash.handler.netty.server;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/19 17:17
 **/
public interface INettyServer extends Runnable {
	/**
	 * 服务名
	 * @return
	 */
	String serverName();
	/***
	 *
	 */
	void shutdown();

	/**
	 * 线程名称
	 * @return
	 */
	String threadName();
}
