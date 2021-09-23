package org.qiunet.data.core.support.db;

import org.qiunet.data.core.support.db.event.DbLoaderOverEventData;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.listener.event.EventHandlerWeightType;
import org.qiunet.listener.event.EventListener;
import org.qiunet.listener.event.data.ServerStartupEventData;

/***
 * db 启动监听处理
 */
class DbStartHandler {
	/** 不少测试的地方. 不需要启动加载数据库. 但是有自动依赖. 使用该参数自动跳过. */
	private static final String SKIP_TEST_START_LOADER = "db.skip_load_db";

	@EventListener(EventHandlerWeightType.HIGHEST)
	public void onServerStartUp(ServerStartupEventData data) {
		if (ServerConfig.instance.getBoolean(SKIP_TEST_START_LOADER)) {
			return;
		}

		DbLoader.getInstance();
		DbLoaderOverEventData.fireEvent();
	}
}
