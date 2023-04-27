package org.qiunet.flash.handler.netty.client.trigger;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.protobuf.ProtobufDataManager;
import org.qiunet.flash.handler.context.header.ISequenceProtocolHeader;
import org.qiunet.flash.handler.context.request.data.ChannelDataMapping;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.session.ClientSession;
import org.qiunet.flash.handler.context.session.ISession;

/***
 *
 * @author qiunet
 * 2023/4/26 17:56
 */
public interface IClientTrigger extends IPersistConnResponseTrigger {

	@Override
	default void response(ISession session, Channel channel, MessageContent data) {
		ClientSession session0 = ((ClientSession) session);
		Class<? extends IChannelData> aClass = ChannelDataMapping.protocolClass(data.getProtocolId());
		IChannelData channelData = ProtobufDataManager.decode(aClass, data.byteBuffer());

		if (session0.isSequenceHeader() && ((ISequenceProtocolHeader) data.getHeader()).sequence() > 0) {
			session0.rsp(((ISequenceProtocolHeader) data.getHeader()).sequence(), channelData);
		}
		response(session0, channelData);
	}

	void response(ClientSession session, IChannelData channelData);
}
