package org.qiunet.flash.handler.netty.server.hook;

import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.utils.logger.LoggerType;

/**
 * 钩子. 先判断shutdown
 * 再判断 reloadCfg
 * 最后判断 继承者的方法 {@link Hook#custom(String)}
 * Created by qiunet.
 * 17/11/22
 */
public interface Hook {
	/**
	 * 默认的端口
	 */
	int DEFAULT_HOOK_PORT = 1314;
	/**
	 * 得到reload的消息
	 * @return
	 */
	default String getReloadCfgMsg(){
			return "RELOAD";
	}
	/***
	 * 调用reload
	 */
	default void reloadCfg() {
		try {
			CfgManagers.getInstance().reloadSetting();
		} catch (Throwable throwable) {
			LoggerType.DUODUO.error("Exception: ", throwable);
		}
	}

	/**
	 * 得到shutdown端口
	 * @return
	 */
	default int getHookPort() {
		if (ServerConfig.getServerPort() > 0) {
			return ServerConfig.getServerPort();
		}else if (ServerConfig.getNodePort() > 0) {
			return ServerConfig.getNodePort();
		}
		LoggerType.DUODUO.error("!!Hook port absent, use default port {}!!", DEFAULT_HOOK_PORT);
		return DEFAULT_HOOK_PORT;
	}
	/**
	 * 返回shutdown的msg
	 * @return
	 */
	default String getShutdownMsg() {
		return "SHUTDOWN0";
	}
	/***
	 * shutdown 时候做的事情.
	 * 自己实现类.
	 */
	void shutdown();
	/***
	 * 用户自定义msg的用途
	 * @param msg
	 */
	void custom(String msg);
}
