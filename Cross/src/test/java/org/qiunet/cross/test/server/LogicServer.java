package org.qiunet.cross.test.server;

import org.qiunet.cross.common.contants.CrossConstants;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.cross.test.common.Constants;
import org.qiunet.cross.test.common.actor.PlayerActor;
import org.qiunet.cross.test.redis.RedisDataUtil;
import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;
import org.qiunet.utils.scanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:24
 */
public class LogicServer {
	private static final Hook hook = new MyHook(9000);

	public static void main(String[] args) {
		ClassScanner.getInstance()
			.addParam(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER, RedisDataUtil::getInstance)
			.addParam(ScannerParamKey.CUSTOM_SERVER_INFO, ServerInfo.valueOf(Constants.LOGIC_SERVER_ID, ServerType.CROSS, Constants.LOGIC_SERVER_PORT, 9002))
			.scanner();

			BootstrapServer.createBootstrap(hook)
				.httpListener(HttpBootstrapParams.custom().setStartupContext(new IStartupContext<PlayerActor>() {
					@Override
					public PlayerActor buildMessageActor(DSession session) {
						return new PlayerActor(session);
					}
				}).setWebsocketPath("/ws").setPort(Constants.LOGIC_SERVER_PORT).setProtocolHeaderType(ProtocolHeaderType.server).build())
				.tcpListener(TcpBootstrapParams.custom().setStartupContext(CrossConstants.DEFAULT_CROSS_NODE_START_CONTEXT).setProtocolHeaderType(ProtocolHeaderType.node).setPort(9002).build())
				.await();
	}

	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
