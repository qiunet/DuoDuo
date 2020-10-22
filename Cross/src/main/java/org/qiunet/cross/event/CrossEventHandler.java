package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.Codec;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.event.BaseUserEventData;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.listener.event.EventManager;
import org.qiunet.listener.event.IEventData;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 * 跨服事件请求
 *
 * @author qiunet
 * 2020-10-15 16:56
 */
@RequestHandler(ID = IProtocolId.System.CROSS_EVENT, desc = "跨服事件处理")
public class CrossEventHandler extends TcpProtobufHandler<AbstractMessageActor, CrossEventRequest> {

	@Override
	public void handler(AbstractMessageActor actor, ITcpRequest<CrossEventRequest> context) throws Exception {
		CrossEventRequest requestData = context.getRequestData();

		Class<? extends IEventData> aClass = (Class<? extends IEventData>) Class.forName(requestData.getClassName());
		Codec<?  extends IEventData> codec = ProtobufDataManager.getCodec(aClass);
		IEventData obj = codec.decode(requestData.getDatas());
		if (obj instanceof BaseUserEventData) {
			((BaseUserEventData) obj).setPlayer((AbstractUserActor) actor);
		}
		EventManager.fireEventHandler(obj);
	}
}
