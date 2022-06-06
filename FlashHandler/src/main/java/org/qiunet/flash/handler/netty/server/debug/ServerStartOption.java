package org.qiunet.flash.handler.netty.server.debug;

import org.qiunet.utils.system.SystemPropertyUtil;

/***
 * 服务启动的一些选项
 * @author qiunet
 * 2022/6/6 16:50
 */
public final class ServerStartOption {
	/**
	 * 是否是开发者模式
	 * 开发者模式下.就是启动一个服务.
	 * 搞定所有服务
	 */
	public static final boolean DEVELOPER_MODE = "true".equalsIgnoreCase(SystemPropertyUtil.get("developer_mode", "false"));
}
