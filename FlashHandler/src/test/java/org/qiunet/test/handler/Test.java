package org.qiunet.test.handler;

import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.test.cross.common.redis.RedisDataUtil;
import org.qiunet.test.handler.bootstrap.hook.MyHook;

/**
 * 用来debug 跟踪内部测试用
 * Created by qiunet.
 * 17/12/15
 */
public class Test {
	private static final Hook hook = new MyHook();
	public static void main(String[] args) throws Exception {
		BootstrapServer.createBootstrap(hook, RedisDataUtil::getInstance).listener(
				ServerBootStrapConfig.newBuild("测试", 8888)
					.encryption()
					.build()
		).await();
	}
}
