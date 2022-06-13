package org.qiunet.flash.handler.context.request.persistconn;


import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.util.ChannelUtil;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractPersistConnRequestContext<RequestData, P extends IMessageActor<P>>
		extends BaseRequestContext<RequestData>
		implements IPersistConnRequestContext<RequestData, P> {

	protected final P messageActor;
	/**
	 * 协议头
	 */
	private final IProtocolHeader protocolHeader;

	protected AbstractPersistConnRequestContext(MessageContent content, Channel channel, P messageActor) {
		super(content, channel);
		this.protocolHeader = content.getHeader();
		this.messageActor = messageActor;
	}

	@Override
	public IProtocolHeader protocolHeader() {
		return protocolHeader;
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
