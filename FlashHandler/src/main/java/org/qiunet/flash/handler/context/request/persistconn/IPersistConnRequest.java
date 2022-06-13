package org.qiunet.flash.handler.context.request.persistconn;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.IRequest;


/**
 * 长连接请求接口
 * Created by qiunet.
 * 17/12/2
 */
public interface IPersistConnRequest<RequestData> extends IRequest<RequestData> {
	/**
	 * netty channel
	 * @return
	 */
	Channel channel();
	/**
	 * 协议头信息
	 * http有可能为空
	 * @return
	 */
	IProtocolHeader protocolHeader();
}
