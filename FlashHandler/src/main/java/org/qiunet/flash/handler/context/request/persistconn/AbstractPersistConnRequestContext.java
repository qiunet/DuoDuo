package org.qiunet.flash.handler.context.request.persistconn;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.header.ISequenceProtocolHeader;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.util.ChannelUtil;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractPersistConnRequestContext<RequestData, P extends IMessageActor<P>>
		extends BaseRequestContext<RequestData>
		implements IPersistConnRequestContext<RequestData, P> {

	protected ISession session;
	/**
	 * 请求协议的序列
	 */
	protected int reqSequence;

	protected void init(ISession session, MessageContent content, Channel channel) {
		super.init(content, channel);
		this.session = session;
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
	public Channel channel() {
		return channel;
	}

	@Override
	public String getRemoteAddress() {
		return ChannelUtil.getIp(channel);
	}
}
