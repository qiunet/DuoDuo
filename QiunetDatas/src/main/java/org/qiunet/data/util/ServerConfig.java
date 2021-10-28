package org.qiunet.data.util;

import org.qiunet.utils.collection.generics.StringSet;
import org.qiunet.utils.config.anno.DConfig;
import org.qiunet.utils.config.anno.DConfigInstance;
import org.qiunet.utils.config.anno.DConfigValue;
import org.qiunet.utils.config.conf.DHocon;
import org.qiunet.utils.data.IKeyValueData;

import java.util.Map;

/***
 * 服务配置
 *
 * @author qiunet
 * 2020-08-04 10:50
 ***/
@DConfig(value = ServerConfig.CONFIG_FILE_NAME)
public enum ServerConfig implements IKeyValueData<String, String> {
	instance;
	// 部分的配置字段名. 外部需要. 所以需要定义.
	public static final String CONFIG_FILE_NAME = "server.conf";
	public static final String HORT_PORT = "server.hook_port";

	@DConfigInstance(CONFIG_FILE_NAME)
	private static DHocon config;

	public static ServerConfig getInstance() {
		return instance;
	}

	@DConfigValue("server.id")
	private static int serverId;

	@DConfigValue("server.type")
	private static ServerType serverType;
	/**
	 * hook 的端口
	 */
	@DConfigValue(HORT_PORT)
	private static int hookPort;
	/**
	 * 对外服务的端口.
	 */
	@DConfigValue("server.server_port")
	private static int serverPort;
	/**
	 * 服务之间通讯的端口.
	 */
	@DConfigValue("server.node_port")
	private static int nodePort;
	/**
	 * 服务器是否开启
	 */
	@DConfigValue(value = "server.open", defaultVal = "TRUE")
	private static boolean serverOpen;
	/**
	 * 服务没有开启提示
	 */
	@DConfigValue(value = "server.close_msg", defaultVal = "-")
	private static String serverCloseMsg;
	/**
	 * 白名单ip. 如果serverOpen = false
	 * 允许指定的ip进入.
	 */
	@DConfigValue(value = "server.ip_white_list", defaultVal = "127.0.0.1")
	private static StringSet ipWhiteList;
	/**
	 * 生成表时候的范围. 必须是这个里面的源才会生成. 功能服和玩法服这里配置不一样
	 */
	@DConfigValue("db.entity_to_table_range")
	private static StringSet entity2TableSourceRange;

	public static int getServerId() {
		return serverId;
	}

	public static ServerType getServerType() {
		return serverType;
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

	public static int getNodePort() {
		return nodePort;
	}

	/**
	 * 是否在需要创建的dbSource范围
	 * @param dbSource
	 * @return
	 */
	public static boolean isDbSourceInRange(String dbSource) {
		return entity2TableSourceRange.contains(dbSource);
	}

	public static DHocon getConfig() {
		return config;
	}

	@Override
	public Map<String, String> returnMap() {
		return config.returnMap();
	}
}
