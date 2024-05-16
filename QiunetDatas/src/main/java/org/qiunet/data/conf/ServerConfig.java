package org.qiunet.data.conf;

import org.qiunet.data.enums.ServerEnvMode;
import org.qiunet.data.enums.ServerType;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.config.anno.DConfig;
import org.qiunet.utils.config.anno.DConfigInstance;
import org.qiunet.utils.config.anno.DConfigValue;
import org.qiunet.utils.config.conf.DHocon;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.listener.event.EventHandlerWeightType;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.scanner.event.ScannerOverEvent;

import java.time.ZoneId;
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
	// 服务器时区信息
	public static final String SERVER_ZONE_ID = "server.current_time_zone_id";
	// 部分的配置字段名. 外部需要. 所以需要定义.
	public static final String CONFIG_FILE_NAME = "server.conf";
	// server port
	public static final String SERVER_PORT = "server.server_port";
	// 游戏服到游戏服-跨服端口. Cross服历史原因.对外是Server_port
	public static final String CROSS_PORT = "server.cross_port";
	// node port
	public static final String NODE_PORT = "server.node_port";
	// secret key
	public static final String SECRET_KEY = "server.secret_key";

	@DConfigInstance(CONFIG_FILE_NAME)
	private static DHocon config;

	public static ServerConfig getInstance() {
		return instance;
	}

	@DConfigValue(value = "server.id", defaultVal = "0")
	private static int serverId;

	private static final LazyLoader<ServerType> serverType = new LazyLoader<>(() -> ServerType.getServerType(serverId));

	/**
	 * 对外服务的端口.
	 */
	@DConfigValue(value = SERVER_PORT, defaultVal = "0")
	private static int serverPort;
	/**
	 * 服务之间通讯的端口.
	 */
	@DConfigValue(value = NODE_PORT, defaultVal = "0")
	private static int nodePort;
	/**
	 * 环境的mode
	 * 需要屏蔽很多测试功能.
	 */
	@DConfigValue(value = "server.env_mode", defaultVal = "OFFICIAL")
	private static ServerEnvMode serverEnvMode;
	/**
	 * 服务器通讯使用的key
	 */
	@DConfigValue(value = SECRET_KEY)
	private static String secretKey;
	/**
	 * 是否是正式服.
	 * 是正式服. 需要屏蔽很多测试功能.
	 */
	public static boolean isOfficial() {
		return serverEnvMode == ServerEnvMode.OFFICIAL;
	}

	public static ServerEnvMode getServerEnvMode() {
		return serverEnvMode;
	}
	/**
	 * 是否是debug 环境.
	 */
	public static boolean isDebugEnv() {
		return serverEnvMode == ServerEnvMode.DEBUG;
	}

	public static int getServerId() {
		return serverId;
	}

	public static String getSecretKey() {
		return secretKey;
	}

	public static ServerType getServerType() {
		return serverType.get();
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static int getNodePort() {
		return nodePort;
	}

	public static DHocon getConfig() {
		return config;
	}

	@Override
	public Map<String, String> returnMap() {
		return config.returnMap();
	}

	@EventListener(EventHandlerWeightType.HIGHEST)
	private void loadOverEvent(ScannerOverEvent event) {
		if (config.containKey(SERVER_ZONE_ID)) {
			DateUtil.setDefaultZoneId(ZoneId.of(config.getString(SERVER_ZONE_ID)));
		}
	}
}
