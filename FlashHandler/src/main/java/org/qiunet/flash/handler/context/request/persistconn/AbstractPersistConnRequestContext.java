package org.qiunet.flash.handler.context.request.persistconn;


import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.header.ISequenceProtocolHeader;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.string.ToString;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractPersistConnRequestContext<RequestData, P extends IMessageActor<P>>
		extends BaseRequestContext<RequestData>
		implements IPersistConnRequestContext<RequestData, P> {

	/**
	 * 请求协议的序列
	 */
	protected int reqSequence;

	protected void init(ISession session, MessageContent content) {
		super.init(session, content);
		if (ISequenceProtocolHeader.class.isAssignableFrom(content.getHeader().getClass())) {
			this.reqSequence = ((ISequenceProtocolHeader) content.getHeader()).sequence();
		}
	}

	/**
	 * 发送消息
	 * @param message channel message
	 */
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush){
		if (reqSequence > 0) {
			message.add(ISequenceProtocolHeader.MESSAGE_KEY, reqSequence);
		}
		return this.getSession().sendMessage(message, flush);
	}


	@Override
	public String getRemoteAddress() {
		return session.getIp();
	}

	@Override
	public String toString() {
		P messageActor = (P) session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		String identifyDesc;
		if (messageActor != null) {
			identifyDesc = messageActor.getIdentity();
		}else {
			identifyDesc = "None-actor";
		}
		return StringUtil.slf4jFormat("[{}]: << {}", identifyDesc, ToString.toString(requestData));
	}
}
