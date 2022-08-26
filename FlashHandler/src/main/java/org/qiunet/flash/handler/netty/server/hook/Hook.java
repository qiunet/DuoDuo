package org.qiunet.flash.handler.netty.server.hook;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.profile.printer.LoggerPrintStream;
import org.qiunet.utils.classLoader.ClassHotSwap;
import org.qiunet.utils.logger.LoggerType;

import java.nio.file.Paths;

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
	/***
	 * 用户自定义msg的用途
	 * @param msg
	 */
	default void custom(String msg) {
		switch (msg) {
			case "hotswap":
				ClassHotSwap.hotSwap(Paths.get(System.getProperty("hotSwap.dir")));
				break;
			case "RequestReferenceEnable":
				ServerConstants.RequestReferenceData.setRecordEnable(true);
				break;
			case "RequestReferenceDisable":
				ServerConstants.RequestReferenceData.setRecordEnable(false);
				break;
			case "RequestReferencePrint":
				ServerConstants.RequestReferenceData.print(new LoggerPrintStream(LoggerType.DUODUO_FLASH_HANDLER.getLogger()));
				break;
		}
	}
}
