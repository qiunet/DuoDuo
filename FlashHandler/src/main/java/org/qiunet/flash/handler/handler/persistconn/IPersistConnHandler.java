package org.qiunet.flash.handler.handler.persistconn;


import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.IHandler;

/**
 * 长连接处理
 * @author qiunet
 *         Created on 17/3/3 12:01.
 */
public interface IPersistConnHandler<P extends IMessageActor, RequestData> extends IHandler<RequestData> {
	/**
	 * tcp udp 处理. 下行一般在逻辑里面处理了
	 * @param context
	 * @return
	 */
	void handler(P playerActor, IPersistConnRequest<RequestData> context)throws Exception;
}
