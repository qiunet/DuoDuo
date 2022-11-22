package org.qiunet.cross.common.contants;

import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;
import org.qiunet.flash.handler.netty.server.config.adapter.IStartupContext;

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
	public static final ServerBootStrapConfig CROSS_SERVER_TCP_BOOTSTRAP_CONFIG = ServerBootStrapConfig.newBuild("跨服玩法服", ServerConfig.getServerPort())
		.setStartupContext(IStartupContext.DEFAULT_CROSS_START_CONTEXT)
		.setReadIdleCheckSeconds(60 * 10)
		.build();
	/**
	 * 服务与服务之间通讯的启动参数
	 */
	public static final ServerBootStrapConfig NODE_SERVER_TCP_BOOTSTRAP_CONFIG = ServerBootStrapConfig.newBuild("节点通讯", ServerConfig.getNodePort())
		.setStartupContext(IStartupContext.DEFAULT_CROSS_NODE_START_CONTEXT)
		.setReadIdleCheckSeconds(60 * 10)
		.build();
}
