package org.qiunet.test.cross.server;

import io.netty.util.ResourceLeakDetector;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.config.adapter.IStartupContext;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.redis.RedisDataUtil;
import org.qiunet.utils.scanner.ClassScanner;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:24
 */
public class CrossServer {
	private static final Hook hook = new MyHook(Constants.CROSS_SERVER_PORT);

	public static void main(String[] args) {
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

		ClassScanner.getInstance(ScannerType.SERVER)
			.addParam(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER, RedisDataUtil::getInstance)
			.addParam(ScannerParamKey.CUSTOM_SERVER_INFO, ServerInfo.valueOf(Constants.CROSS_SERVER_ID, Constants.CROSS_SERVER_PORT, Constants.CROSS_NODE_PORT))
			.scanner();

			BootstrapServer.createBootstrap(hook)
				.listener(ServerBootStrapConfig.newBuild("跨服服务", Constants.CROSS_SERVER_PORT).setStartupContext(IStartupContext.DEFAULT_CROSS_START_CONTEXT).build())
				.listener(ServerBootStrapConfig.newBuild("跨服节点", Constants.CROSS_NODE_PORT).setStartupContext(IStartupContext.DEFAULT_SERVER_NODE_START_CONTEXT).build())
				.await();
	}

	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
