package org.qiunet.flash.handler.netty.transmit;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 20:22
 */
@RequestHandler(ID = IProtocolId.System.PLAYER_2_CROSS_TRANSMIT_REQ, desc = "玩家请求转发到Cross")
public class CrossTransmitHandler extends TcpProtobufHandler<AbstractUserActor, TransmitRequest> {

	@Override
	public void handler(AbstractUserActor playerActor, ITcpRequest<TransmitRequest> context) throws Exception {
		TransmitRequest requestData = context.getRequestData();
		IHandler handler0 = RequestHandlerMapping.getInstance().getHandler(requestData.getPid());

		Preconditions.checkState(handler0 instanceof ITransmitHandler,
			"protocolId [%s] handler[%s] not a transmit handler", requestData.getPid(), handler0 == null ? "": handler0.getClass().getName());
		Object data = handler0.parseRequestData(requestData.getBytes());
		((ITransmitHandler) handler0).crossHandler(playerActor, data);
	}
}
