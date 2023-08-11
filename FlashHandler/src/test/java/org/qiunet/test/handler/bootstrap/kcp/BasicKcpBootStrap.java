package org.qiunet.test.handler.bootstrap.kcp;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.hook.DefaultHook;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.test.cross.common.redis.RedisDataUtil;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/25
 */
public abstract class BasicKcpBootStrap {
	protected static final String host = "localhost";
	protected static final int port = 8888;
	protected static final Hook hook = new DefaultHook();
	protected static Thread currThread;

	@BeforeAll
	public static void init() throws Exception {
		currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			BootstrapServer server = BootstrapServer.createBootstrap(hook, RedisDataUtil::getInstance)
			.listener(ServerBootStrapConfig.newBuild("游戏服", port)
				.setKcpBootStrapConfig(ServerBootStrapConfig.KcpBootstrapConfig.newBuild().setPortCount(0, 1).build())
				.encryption()
				.build());
			server.await(() -> LockSupport.unpark(currThread));
		});
		thread.start();
		LockSupport.park();
	}

	@AfterAll
	public static void shutdown() {
		System.gc();
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
