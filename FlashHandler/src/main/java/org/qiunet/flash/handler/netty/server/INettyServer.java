package org.qiunet.flash.handler.netty.server;

import io.netty.util.concurrent.Promise;
import org.qiunet.utils.async.future.DFuture;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/19 17:17
 **/
public interface INettyServer extends Runnable {
	/**
	 * 是否启动成功
	 * @return true 成功
	 */
	Promise<Void> successFuture();
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
