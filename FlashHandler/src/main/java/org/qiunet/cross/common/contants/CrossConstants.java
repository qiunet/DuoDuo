package org.qiunet.cross.common.contants;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.netty.server.param.ServerBootStrapParam;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/***
 *
 *
 * @author qiunet
 * 2020-10-21 18:28
 */
public final class CrossConstants {

	/**
	 * cross 服务 tcp 默认启动参数
	 */
	public static final ServerBootStrapParam CROSS_SERVER_TCP_BOOTSTRAP_PARAMS = ServerBootStrapParam.newBuild("跨服玩法服", ServerConfig.getServerPort())
		.setStartupContext(IStartupContext.DEFAULT_CROSS_START_CONTEXT)
		.setReadIdleCheckSeconds(60 * 10)
		.build();
	/**
	 * 服务与服务之间通讯的启动参数
	 */
	public static final ServerBootStrapParam NODE_SERVER_TCP_BOOTSTRAP_PARAMS = ServerBootStrapParam.newBuild("节点通讯", ServerConfig.getNodePort())
		.setStartupContext(IStartupContext.DEFAULT_CROSS_NODE_START_CONTEXT)
		.setReadIdleCheckSeconds(60 * 10)
		.build();
}
