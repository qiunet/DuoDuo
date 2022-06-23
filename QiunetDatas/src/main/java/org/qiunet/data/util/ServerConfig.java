package org.qiunet.data.util;

import org.qiunet.data.core.enums.ServerEnvMode;
import org.qiunet.utils.async.LazyLoader;
import org.qiunet.utils.collection.generics.StringList;
import org.qiunet.utils.config.anno.DConfig;
import org.qiunet.utils.config.anno.DConfigInstance;
import org.qiunet.utils.config.anno.DConfigValue;
import org.qiunet.utils.config.conf.DHocon;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.exceptions.CustomException;

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
	// server port
	public static final String SERVER_PORT = "server.server_port";
	// 游戏服到游戏服-跨服端口. Cross服历史原因.对外是Server_port
	public static final String CROSS_PORT = "server.cross_port";
	// node port
	public static final String NODE_PORT = "server.node_port";
	// secret key
	public static final String SECRET_KEY = "server.secret_key";
	// 生成数据表的范围
	public static final String ENTITY_TO_TABLE_RANGE = "db.entity_to_table_range";

	@DConfigInstance(CONFIG_FILE_NAME)
	private static DHocon config;

	public static ServerConfig getInstance() {
		return instance;
	}

	@DConfigValue(value = "server.id", defaultVal = "0")
	private static int serverId;
	/**
	 * 组ID
	 */
	private static final LazyLoader<Integer> serverGroupId = new LazyLoader<>(() -> ServerType.getGroupId(serverId));

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
	 * 生成表时候的范围. 必须是这个里面的源才会生成. 功能服和玩法服这里配置不一样
	 */
	@DConfigValue(value = ENTITY_TO_TABLE_RANGE, defaultVal = "")
	private static StringList entity2TableSourceRange;
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

	public static int getServerId() {
		return serverId;
	}

	public static String getSecretKey() {
		return secretKey;
	}

	/**
	 * 得到服务器组ID
	 * @return
	 */
	public static int getServerGroupId(){
		return serverGroupId.get();
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

	/**
	 * 是否在需要创建的dbSource范围
	 * @param dbSource
	 * @return
	 */
	public static boolean isDbSourceInRange(String dbSource) {
		return entity2TableSourceRange.contains(dbSource);
	}

	/**
	 * 得到默认源 默认为entity_to_table_range的第一个.
	 * 如果有的话
	 * @return
	 */
	public static String getDefaultDbSource() {
		if (entity2TableSourceRange.isEmpty()) {
			throw new CustomException("entity_to_table_range is empty!");
		}
		return entity2TableSourceRange.get(0);
	}

	public static DHocon getConfig() {
		return config;
	}

	@Override
	public Map<String, String> returnMap() {
		return config.returnMap();
	}
}
