package org.qiunet.flash.handler.netty.server.hook;

import org.qiunet.data.conf.ServerConfig;
import org.qiunet.utils.logger.LoggerType;

/**
 * 钩子. 先判断shutdown
 * 再判断 reloadCfg
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
	/**
	 * 让该服务器过期
	 * @return
	 */
	default String getDeprecateMsg(){
		return "DEPRECATE";
	}
	/**
	 * 停止服务(不是shutdown)
	 * @return
	 */
	default String getServerCloseMsg(){
		return "CLOSE";
	}
	/**
	 * 返回shutdown的msg
	 * @return
	 */
	default String getShutdownMsg() {
		return "SHUTDOWN0";
	}


	default String hotswapMsg() {
		return "HOTSWAP";
	}
	/**
	 * 得到shutdown端口
	 * @return
	 */
	default int getHookPort() {
		// Server Port 可能开了udp. 优先使用 node port.
		if (ServerConfig.getNodePort() > 0) {
			return ServerConfig.getNodePort();
		}else if (ServerConfig.getServerPort() > 0) {
			return ServerConfig.getServerPort();
		}
		LoggerType.DUODUO.error("!!Hook port absent, use default port {}!!", DEFAULT_HOOK_PORT);
		return DEFAULT_HOOK_PORT;
	}
}
