package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.Codec;
import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 * 跨服事件请求
 *
 * @author qiunet
 * 2020-10-15 16:56
 */
@RequestHandler(ID = IProtocolId.System.CROSS_EVENT, desc = "跨服事件处理")
public class CrossEventHandler extends TcpProtobufHandler<CrossPlayerActor, CrossEventRequest> {

	@Override
	public void handler(CrossPlayerActor playerActor, ITcpRequest<CrossEventRequest> context) throws Exception {
		CrossEventRequest requestData = context.getRequestData();

		Class<?> aClass = Class.forName(requestData.getClassName());
		Codec<?> codec = ProtobufDataManager.getCodec(aClass);
		BasePlayerEventData obj = (BasePlayerEventData) codec.decode(requestData.getDatas());
	}
}
