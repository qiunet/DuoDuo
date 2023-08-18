package org.qiunet.flash.handler.common;

import java.io.Serializable;

/***
 * MessageHandler执行的一个对象.
 *
 * @author qiunet
 * 2020-02-08 20:56
 **/
@FunctionalInterface
public interface IMessage<H extends IMessageHandler<H>> extends Serializable {
	/***
	 * 执行
	 * @param h 对象本身
	 */
	void execute(H h) throws Exception;
}
