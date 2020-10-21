package org.qiunet.cross.common.contants;

import org.qiunet.cross.common.config.ServerConfig;
import org.qiunet.cross.common.start.DefaultCommunicationStartUpContext;
import org.qiunet.cross.common.start.DefaultCrossStartUpContext;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

/***
 *
 *
 * @author qiunet
 * 2020-10-21 18:28
 */
public class CrossConstants {
	/**
	 * cross 服务 tcp 默认启动参数
	 */
	public static final TcpBootstrapParams CROSS_SERVER_TCP_BOOTSTRAP_PARAMS = TcpBootstrapParams.custom()
		.setStartupContext(new DefaultCrossStartUpContext())
		.setPort(ServerConfig.getServerPort())
		.build();
	/**
	 * 服务与服务之间通讯的启动参数
	 */
	public static final TcpBootstrapParams COMMUNICATION_SERVER_TCP_BOOTSTRAP_PARAMS = TcpBootstrapParams.custom()
		.setStartupContext(new DefaultCommunicationStartUpContext())
		.setPort(ServerConfig.getCommunicationPort())
		.build();
}
