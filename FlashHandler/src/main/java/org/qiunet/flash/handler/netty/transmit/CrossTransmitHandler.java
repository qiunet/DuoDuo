package org.qiunet.flash.handler.netty.transmit;

import com.google.common.base.Preconditions;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.context.request.data.pb.PbChannelDataMapping;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

import java.nio.ByteBuffer;

/***
 *
 *
 * @author qiunet
 * 2020-10-26 20:22
 */

public class CrossTransmitHandler extends PersistConnPbHandler<AbstractUserActor, TransmitRequest> {

	@Override
	public void handler(AbstractUserActor playerActor, IPersistConnRequest<TransmitRequest> context) throws Exception {
		TransmitRequest requestData = context.getRequestData();
		IHandler handler0 = PbChannelDataMapping.getHandler(requestData.getPid());

		Preconditions.checkState(handler0 instanceof ITransmitHandler,
			"protocolId [%s] handler[%s] not a transmit handler", requestData.getPid(), handler0 == null ? "": handler0.getClass().getName());
		Object data = handler0.parseRequestData(ByteBuffer.wrap(requestData.getBytes()));
		((ITransmitHandler) handler0).crossHandler(playerActor, data);
	}
}
