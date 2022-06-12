package org.qiunet.flash.handler.netty.transmit;

import org.qiunet.flash.handler.common.player.AbstractUserActor;

/***
 * 可以转发请求的handler
 *
 * @author qiunet
 * 2020-10-26 17:20
 */
public interface ITransmitHandler<P extends AbstractUserActor<P>, RequestData> {
	/**
	 * 处理跨服的方法.
	 * @param actor
	 * @param requestData
	 */
	void crossHandler(P actor, RequestData requestData);
}
