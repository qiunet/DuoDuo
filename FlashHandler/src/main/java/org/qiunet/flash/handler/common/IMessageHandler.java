package org.qiunet.flash.handler.common;

/***
 *
 * @author qiunet
 * 2020-04-12 12:51
 **/
public interface IMessageHandler<H extends IMessageHandler> {
	/**
	 *
	 * @param message
	 */
	void addMessage(IMessage<H> message);
}
