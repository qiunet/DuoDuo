package org.qiunet.test.cross.server;

import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
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
public class CrossServer {
	private static final Hook hook = new MyHook(Constants.CROSS_SERVER_PORT);

	public static void main(String[] args) {
		ClassScanner.getInstance(ScannerType.SERVER)
			.addParam(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER, RedisDataUtil::getInstance)
			.addParam(ScannerParamKey.CUSTOM_SERVER_INFO, ServerInfo.valueOf(Constants.CROSS_SERVER_ID, Constants.CROSS_SERVER_PORT, Constants.CROSS_NODE_PORT))
			.scanner();

			BootstrapServer.createBootstrap(hook)
				.tcpListener(TcpBootstrapParams.custom().setStartupContext(IStartupContext.DEFAULT_CROSS_START_CONTEXT).setProtocolHeaderType(ProtocolHeaderType.cross).setPort(Constants.CROSS_SERVER_PORT).build())
				.tcpListener(TcpBootstrapParams.custom().setStartupContext(IStartupContext.DEFAULT_CROSS_NODE_START_CONTEXT).setProtocolHeaderType(ProtocolHeaderType.node).setPort(Constants.CROSS_NODE_PORT).build())
				.await();
	}

	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
