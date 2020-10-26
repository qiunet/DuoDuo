package org.qiunet.flash.handler.common.player;

import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.message.MessageContent;

/***
 * 消息发送往Cross服务的接口
 *
 * @author qiunet
 * 2020-10-26 14:54
 */
public interface IMessageToCross {
	/**
	 * 是否跨服状态
	 * @return
	 */
	boolean isCrossStatus();
	/**
	 * 发送消息.
	 * @param content
	 * @return
	 */
	ChannelFuture writeToCross(MessageContent content);
}
