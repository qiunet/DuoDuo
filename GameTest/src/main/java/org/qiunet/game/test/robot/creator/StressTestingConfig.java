package org.qiunet.game.test.robot.creator;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.utils.config.anno.DConfig;
import org.qiunet.utils.config.anno.DConfigInstance;
import org.qiunet.utils.config.anno.DConfigValue;
import org.qiunet.utils.config.conf.DHocon;

/***
 * 压测配置
 * 配置在server.conf
 * 内容:
 * robot: {
 * 	// 数量
 * 	count: 1
 * 	// 是否日志
 * 	log: true
 * 	// tick间隔时间 (毫秒)
 * 	tick: 1000
 * 	// 创建间隔毫秒
 * 	interval: 150
 *	// 监听端口
 * 	hook: 15050
 * }
 *
 * @author qiunet
 * 2022/8/25 11:44
 */
@DConfig(ServerConfig.CONFIG_FILE_NAME)
public class StressTestingConfig {
	/*文件名*/
	public static final String STRESS_TESTING_CONFIG_HOOK_PORT = "robot.hook";
	/**
	 * 数量
	 */
	@DConfigValue(value = "robot.count", defaultVal = "1")
	private static int count;
	/**
	 * 决策信息是否打印
	 */
	@DConfigValue(value = "robot.log", defaultVal = "false")
	private static boolean logPolicy;
	/**
	 * 机器人决策间隔毫秒
	 */
	@DConfigValue(value = "robot.tick", defaultVal = "1000")
	private static int tick;
	/**
	 * 创建机器人的间隔.
	 */
	@DConfigValue(value = "robot.interval", defaultVal = "300")
	private static int interval;
	/**
	 * 监听端口
	 */
	@DConfigValue(value = STRESS_TESTING_CONFIG_HOOK_PORT, defaultVal = "15050")
	private static int hookPort;

	@DConfigInstance(ServerConfig.CONFIG_FILE_NAME)
	private static DHocon config;

	private StressTestingConfig() {}
	/**
	 * 数量
	 */
	public static int getCount() {
		return count;
	}
	/**
	 * 机器人决策间隔毫秒
	 */
	public static boolean isLogPolicy() {
		return logPolicy;
	}

	/**
	 * hookPort
	 * @return
	 */
	public static int getHookPort() {
		return hookPort;
	}

	/**
	 * 机器人决策间隔毫秒
	 */
	public static int getTick() {
		return tick;
	}
	/**
	 * 创建机器人的间隔.
	 */
	public static int getInterval() {
		return interval;
	}
}
