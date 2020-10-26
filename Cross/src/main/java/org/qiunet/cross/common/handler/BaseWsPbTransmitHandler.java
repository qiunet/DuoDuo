package org.qiunet.cross.common.handler;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.annotation.TransmitHandler;
import org.qiunet.flash.handler.common.player.AbstractPlayerActor;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.context.request.websocket.IWebSocketRequest;
import org.qiunet.flash.handler.handler.websocket.WebSocketProtobufHandler;

/***
 * 标识转发的handler
 *  如果是跨服的状态.
 *  handler处理会直接转发到跨服服务.
 *
 * @author qiunet
 * 2020-10-26 15:24
 */
@TransmitHandler
public abstract class BaseWsPbTransmitHandler<ACTOR extends AbstractPlayerActor<ACTOR>, REQ extends IpbRequestData> extends WebSocketProtobufHandler<AbstractUserActor, REQ> {

	@Override
	public void handler(AbstractUserActor playerActor, IWebSocketRequest<REQ> context) throws Exception {
		if (! playerActor.isCrossStatus()) {
			this.logicHandler(((ACTOR) playerActor), context);
		}else {
			this.crossHandler(((CrossPlayerActor) playerActor), context);
		}
	}

	/***
	 * 逻辑服处理
	 * @param playerActor
	 * @param context
	 * @throws Exception
	 */
	protected abstract void logicHandler(ACTOR playerActor, IWebSocketRequest<REQ> context) throws Exception;

	/**
	 * 跨服的处理
	 * @param crossPlayerActor
	 * @param context
	 * @throws Exception
	 */
	protected abstract void crossHandler(CrossPlayerActor crossPlayerActor, IWebSocketRequest<REQ> context) throws Exception;

}
