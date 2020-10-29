package org.qiunet.data.util;

import org.qiunet.utils.properties.anno.DProperties;
import org.qiunet.utils.properties.anno.DPropertiesValue;

/***
 *
 *
 * @author qiunet
 * 2020-08-04 10:50
 ***/
@DProperties(ServerConfig.PROPERTIES_FILE_NAME)
public class ServerConfig {
	public static final String PROPERTIES_FILE_NAME = "server.properties";
	public static final String HORT_PORT = "hook.port";
	public static final String SERVER_ID = "server.id";
	public static final String SERVER_TYPE = "server.type";
	public static final String SERVER_PORT = "server.port";
	public static final String COMMUNICATION_PORT = "communication.port";

	@DPropertiesValue(SERVER_ID)
	private static int serverId;

	@DPropertiesValue(SERVER_TYPE)
	private static ServerType serverType;
	/**
	 * hook 的端口
	 */
	@DPropertiesValue(HORT_PORT)
	private static int hookPort;
	/**
	 * 对外服务的端口.
	 */
	@DPropertiesValue(SERVER_PORT)
	private static int serverPort;
	/**
	 * 服务之间通讯的端口.
	 */
	@DPropertiesValue(COMMUNICATION_PORT)
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
