package org.qiunet.utils.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志类型
 * Created by qiunet.
 * 17/8/18
 */
public enum  LoggerType implements ILoggerType {
	/**普通的日志打印*/
	DUODUO("DuoDuo"),
	/**在线打印*/
	DUODUO_ONLINE("DuoDuoOnline"),
	/**跨服日志**/
	DUODUO_CROSS("DuoDuoCross"),
	/***http请求**/
	DUODUO_HTTP("DuoDuoHttp"),
	/**sql日志打印*/
	DUODUO_SQL("DuoDuoSql"),
	/**redis 的 日志打印*/
	DUODUO_REDIS("DuoDuoRedis"),
	/***热替换的日志打印**/
	DUODUO_HOTSWAP("DuoDuoHotSwap"),
	/**自动生成的日志打印 */
	DUODUO_CREATOR("DuoDuoCreator"),
	/**用时警告等打印*/
	DUODUO_USE_TIME("DuoDuoUseTime"),
	/**游戏测试日志*/
	DUODUO_GAME_TEST("DuoDuoGameTest"),
	/** 读配置的日志打印 */
	DUODUO_CFG_READER("DuoDuoCfgReader"),
	/** flashHandler 的日志*/
	DUODUO_FLASH_HANDLER("DuoDuoFlashHandler"),
	;

	private String loggerName;

	LoggerType(String loggerName) {
		this.loggerName = loggerName;
	}

	@Override
	public Logger getLogger() {
		return LoggerFactory.getLogger(loggerName);
	}
}
