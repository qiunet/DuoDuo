package org.qiunet.test.cross.server;

import io.netty.util.ResourceLeakDetector;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;
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
public class LogicServer {
	private static final Hook hook = new MyHook(Constants.LOGIC_SERVER_PORT);

	public static void main(String[] args) {
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

		ClassScanner.getInstance(ScannerType.SERVER)
			.addParam(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER, RedisDataUtil::getInstance)
			.addParam(ScannerParamKey.CUSTOM_SERVER_INFO, ServerInfo.valueOf(Constants.LOGIC_SERVER_ID, Constants.LOGIC_SERVER_PORT, Constants.LOGIC_NODE_PORT))
			.scanner();

			BootstrapServer.createBootstrap(hook)
				.httpListener(HttpBootstrapParams.custom().setStartupContext(new IStartupContext<PlayerActor>() {
					@Override
					public PlayerActor buildMessageActor(ISession session) {
						return new PlayerActor(session);
					}
				}).setWebsocketPath("/ws").setPort(Constants.LOGIC_SERVER_PORT).setProtocolHeaderType(ProtocolHeaderType.server).build())
				.tcpListener(TcpBootstrapParams.custom().setStartupContext(IStartupContext.DEFAULT_CROSS_NODE_START_CONTEXT).setProtocolHeaderType(ProtocolHeaderType.node).setPort(9002).build())
				.await();
	}

	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
