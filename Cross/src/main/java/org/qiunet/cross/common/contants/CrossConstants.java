package org.qiunet.cross.common.contants;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.cross.common.config.ServerConfig;
import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/***
 *
 *
 * @author qiunet
 * 2020-10-21 18:28
 */
public final class CrossConstants {
	/**
	 * 默认的cross 启动上下文
	 */
	public static final IStartupContext<CrossPlayerActor> DEFAULT_CROSS_START_CONTEXT = CrossPlayerActor::new;
	/**
	 * 默认的cross server node 启动上下文
	 */
	public static final IStartupContext<ServerNode> DEFAULT_CROSS_NODE_START_CONTEXT = ServerNode::new;
	/**
	 * cross 服务 tcp 默认启动参数
	 */
	public static final TcpBootstrapParams CROSS_SERVER_TCP_BOOTSTRAP_PARAMS = TcpBootstrapParams.custom()
		.setStartupContext(DEFAULT_CROSS_START_CONTEXT)
		.setPort(ServerConfig.getServerPort())
		.build();
	/**
	 * 服务与服务之间通讯的启动参数
	 */
	public static final TcpBootstrapParams COMMUNICATION_SERVER_TCP_BOOTSTRAP_PARAMS = TcpBootstrapParams.custom()
		.setStartupContext(DEFAULT_CROSS_NODE_START_CONTEXT)
		.setPort(ServerConfig.getCommunicationPort())
		.build();
}
