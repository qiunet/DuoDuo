package org.qiunet.test.cross.server;

import io.netty.util.ResourceLeakDetector;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.redis.RedisDataUtil;
import org.qiunet.utils.scanner.ScannerType;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:24
 */
public class CrossServer {
	private static final Hook hook = new MyHook(Constants.CROSS_NODE_PORT);

	public static void main(String[] args) {
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
		BootstrapServer.createBootstrap(hook, RedisDataUtil::getInstance, ScannerType.SERVER, classScanner -> {
			classScanner.addParam(ScannerParamKey.CUSTOM_SERVER_INFO, ServerInfo.valueOf(Constants.CROSS_SERVER_ID, 0, Constants.CROSS_NODE_PORT));
		}).await();
	}

	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
