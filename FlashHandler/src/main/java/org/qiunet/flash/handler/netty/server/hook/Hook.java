package org.qiunet.flash.handler.netty.server.hook;

/**
 * 钩子. 先判断shutdown
 * 再判断 reloadCfg
 * 最后判断 继承者的方法 {@link Hook#custom(String, String)}
 * Created by qiunet.
 * 17/11/22
 */
public interface Hook {
	/**
	 * 得到reload的消息
	 * @return
	 */
	String getReloadCfgMsg();
	/***
	 * 调用reload
	 */
	void reloadCfg();

	/**
	 * 得到shutdown端口
	 * @return
	 */
	int getHookPort();
	/**
	 * 返回shutdown的msg
	 * @return
	 */
	String getShutdownMsg();
	/***
	 * shudown 时候做的事情.
	 * 自己实现类.
	 */
	void shutdown();
	/***
	 * 用户自定义msg的用途
	 * @param msg
	 */
	void custom(String msg, String ip);
}
