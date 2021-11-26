package org.qiunet.cross.test.server;

import org.qiunet.cross.common.contants.CrossConstants;
import org.qiunet.cross.common.contants.ScannerParamKey;
import org.qiunet.cross.node.ServerInfo;
import org.qiunet.cross.test.common.Constants;
import org.qiunet.cross.test.common.redis.RedisDataUtil;
import org.qiunet.flash.handler.context.header.ProtocolHeaderType;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.scanner.ClassScanner;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:24
 */
public class CrossServer {
	private static final Hook hook = new MyHook(Constants.CROSS_SERVER_PORT);

	public static void main(String[] args) {
		ClassScanner.getInstance()
			.addParam(ScannerParamKey.SERVER_NODE_REDIS_INSTANCE_SUPPLIER, RedisDataUtil::getInstance)
			.addParam(ScannerParamKey.CUSTOM_SERVER_INFO, ServerInfo.valueOf(Constants.CROSS_SERVER_ID, Constants.CROSS_SERVER_PORT, Constants.CROSS_NODE_PORT))
			.scanner();

			BootstrapServer.createBootstrap(hook)
				.tcpListener(TcpBootstrapParams.custom().setStartupContext(CrossConstants.DEFAULT_CROSS_START_CONTEXT).setProtocolHeaderType(ProtocolHeaderType.node).setPort(8001).build())
				.tcpListener(TcpBootstrapParams.custom().setStartupContext(CrossConstants.DEFAULT_CROSS_NODE_START_CONTEXT).setProtocolHeaderType(ProtocolHeaderType.node).setPort(8002).build())
				.await();
	}

	public static void shutdown(){
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}
