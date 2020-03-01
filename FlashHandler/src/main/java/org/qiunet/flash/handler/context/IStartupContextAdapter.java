package org.qiunet.flash.handler.context;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeader;
import org.qiunet.flash.handler.context.session.ISession;

/***
 * 创建启动上下文
 * @author qiunet
 */
public interface IStartupContextAdapter< S extends ISession, T extends IPlayerActor<S>> {
	/***
	 * 使用bytes 和 protocolId 构造一个 ProtocolHeader
	 * @param content
	 * @return
	 */
	default IProtocolHeader newHeader(MessageContent content) {
		return new DefaultProtocolHeader(content);
	}
	/***
	 * 使用
	 * @param in
	 * @return
	 */
	default IProtocolHeader newHeader(ByteBuf in){
		return new DefaultProtocolHeader(in);
	}
	/**
	 * 得到头的长度
	 * @return
	 */
	default int getHeaderLength(){
		return DefaultProtocolHeader.REQUEST_HEADER_LENGTH;
	}

	/**
	 * 构造session
	 * @param channel
	 * @return
	 */
	T buildSession(Channel channel);

	/**
	 * 构造PlayerActor
	 * @param session
	 * @return
	 */
	T buildPlayerActor(S session);
}
