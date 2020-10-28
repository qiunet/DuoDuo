package org.qiunet.data.util;

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

	@DPropertiesValue("server.id")
	private static int serverId;

	@DPropertiesValue("server.type")
	private static ServerType serverType;
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
	private static int communicationPort;

	public static int getServerId() {
		return serverId;
	}

	public static ServerType getServerType() {
		return serverType;
	}

	public static boolean isLogicServerType() {
		return getServerType() == ServerType.LOGIC || getServerType() == ServerType.ALL;
	}

	public static int getHookPort() {
		return hookPort;
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static int getCommunicationPort() {
		return communicationPort;
	}
}
