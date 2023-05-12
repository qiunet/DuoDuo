package org.qiunet.game.tests.server;


import org.qiunet.flash.handler.context.header.SequenceIdProtocolHeader;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.hook.DefaultHook;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.game.tests.server.enums.ServerType;
import org.qiunet.game.tests.server.redis.RedisDataUtil;
import org.qiunet.utils.scanner.ScannerType;

import java.util.concurrent.locks.LockSupport;

/**
 * 测试时候启动服务
 * Created by qiunet.
 * 17/12/9
 */
public final class ServerStartup {
	private static final Hook hook = new DefaultHook();
	private boolean startuped;
	public void startup() {
		if (this.startuped) throw new IllegalStateException("server was startup.");
		this.startuped = true;

		final Thread currThread = Thread.currentThread();
		Thread thread = new Thread(() -> {
			BootstrapServer server = BootstrapServer.createBootstrap(hook, RedisDataUtil::getInstance, ScannerType.GAME_TEST);
			server.listener(ServerBootStrapConfig.newBuild("压测服务测试", ServerType.LC_ROOM.port())
					.setProtocolHeader(SequenceIdProtocolHeader.instance)
					.build());
			server.await(() -> LockSupport.unpark(currThread));
		}, "Client-Server-Startup");
		thread.start();
		LockSupport.park();
	}

	public void shutdown(){
		if (!startuped) throw new IllegalStateException("Server was shutdown .");
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
