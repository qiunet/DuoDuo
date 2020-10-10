package org.qiunet.cross.common.config;

import org.qiunet.utils.properties.anno.DProperties;
import org.qiunet.utils.properties.anno.DPropertiesValue;

/***
 *
 *
 * @author qiunet
 * 2020-08-04 10:50
 ***/
@DProperties("server.properties")
public class ServerConfig {
	/**
	 * hook 的端口
	 */
	@DPropertiesValue("hook.port")
	private static int hookPort;
	/**
	 * 对外服务的端口.
	 */
	@DPropertiesValue("server.port")
	private static int serverPort;
	/**
	 * 服务之间通讯的端口.
	 */
	@DPropertiesValue("communication.port")
	private static int commnicationPort;

	public static int getHookPort() {
		return hookPort;
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static int getCommnicationPort() {
		return commnicationPort;
	}
}
