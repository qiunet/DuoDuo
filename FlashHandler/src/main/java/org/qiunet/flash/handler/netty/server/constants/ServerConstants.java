package org.qiunet.flash.handler.netty.server.constants;

import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.enums.HandlerType;

/**
 * Created by qiunet.
 * 2019-03-23 21:33
 */
public class ServerConstants {
	/***
	 * channel 存储他是使用哪种方式连接的.
	 */
	public static final AttributeKey<HandlerType> HANDLER_TYPE_KEY = AttributeKey.newInstance("HANDLER_TYPE_KEY");
}
