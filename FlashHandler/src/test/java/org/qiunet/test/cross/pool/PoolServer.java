package org.qiunet.test.cross.pool;

import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.redis.RedisDataUtil;
import org.qiunet.test.cross.server.MyHook;
import org.qiunet.utils.scanner.ScannerType;

/**
 * @author qiunet
 * 2023/11/18 22:25
 */
public class PoolServer {
	private static final Hook hook = new MyHook(Constants.CROSS_NODE_PORT);

	public static void main(String[] args) {
		ServerInfo serverInfo = ServerInfo.valueOf(Constants.CROSS_SERVER_ID, 0, Constants.CROSS_NODE_PORT);
		BootstrapServer
			.createBootstrap(hook, RedisDataUtil::getInstance, ScannerType.SERVER,
				classScanner -> classScanner.addParam(ScannerParamKey.CUSTOM_SERVER_INFO, serverInfo))
			.await();
	}
}
