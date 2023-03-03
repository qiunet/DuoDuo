package org.qiunet.flash.handler.common;

/***
 * MessageHandler执行的一个对象.
 *
 * @author qiunet
 * 2020-02-08 20:56
 **/
public interface IMessage<H extends IMessageHandler<H>> {
	/***
	 * 执行
	 * @param h
	 */
	void execute(H h) throws Exception;
}
