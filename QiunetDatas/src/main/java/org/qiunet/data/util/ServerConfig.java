package org.qiunet.data.util;

import org.qiunet.utils.collection.generics.StringSet;
import org.qiunet.utils.properties.anno.DProperties;
import org.qiunet.utils.properties.anno.DPropertiesValue;

/***
 *
 *
 * @author qiunet
 * 2020-08-04 10:50
 ***/
@DProperties(value = ServerConfig.PROPERTIES_FILE_NAME, listenerChange = true)
public class ServerConfig {
	public static final String HORT_PORT = "hook.port";
	public static final String SERVER_ID = "server.id";
	public static final String SERVER_TYPE = "server.type";
	public static final String SERVER_PORT = "server.port";
	public static final String SERVER_OPEN = "server.open";
	public static final String SERVER_CLOSE_MSG = "server.close_msg";
	public static final String COMMUNICATION_PORT = "communication.port";
	public static final String PROPERTIES_FILE_NAME = "server.properties";
	public static final String SERVER_IP_WHITE_LIST = "server.ip.white.list";

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
	/**
	 * 服务器是否开启
	 */
	@DPropertiesValue(value = SERVER_OPEN, defaultVal = "TRUE")
	private static boolean serverOpen;
	/**
	 * 服务没有开启提示
	 */
	@DPropertiesValue(value = SERVER_CLOSE_MSG, defaultVal = "-")
	private static String serverCloseMsg;
	/**
	 * 白名单ip. 如果serverOpen = false
	 * 允许指定的ip进入.
	 */
	@DPropertiesValue(value = SERVER_IP_WHITE_LIST, defaultVal = "127.0.0.1")
	private static StringSet ipWhiteList;

	public static int getServerId() {
		return serverId;
	}

	public static ServerType getServerType() {
		return serverType;
	}

	public static boolean isLogicServerType() {
		return getServerType() == ServerType.LOGIC || getServerType() == ServerType.ALL;
	}

	public static String getServerCloseMsg() {
		return serverCloseMsg;
	}

	public static StringSet getIpWhiteList() {
		return ipWhiteList;
	}

	public static boolean isServerOpen() {
		return serverOpen;
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
