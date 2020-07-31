package org.qiunet.flash.handler.common;

import org.qiunet.utils.async.future.DFuture;
import org.qiunet.utils.date.DateUtil;

import java.util.concurrent.TimeUnit;

/***
 *
 * @author qiunet
 * 2020-04-12 12:51
 **/
public interface IMessageHandler<H extends IMessageHandler> {
	/**
	 * 销毁不用了必须调用
	 */
	void destroy();
	/**
	 *
	 * @param message
	 */
	void addMessage(IMessage<H> message);

	/**
	 * 在某个毫秒时间戳执行
	 * @param msg
	 * @param executeMillis 执行的毫秒时间戳
	 * @return
	 */
	default DFuture<Void> scheduleMessage(IMessage<H> msg, long executeMillis){
		long now = DateUtil.currentTimeMillis();
		if (executeMillis < now) {
			throw new IllegalStateException("executeMillis ["+executeMillis+"] is less than current time ["+now+"]");
		}
		return this.scheduleMessage(msg, (executeMillis - now), TimeUnit.MILLISECONDS);
	}
	/**
	 *指定一定的延迟时间后, 执行该消息
	 * @param msg
	 * @param delay
	 * @param unit
	 * @return
	 */
	DFuture<Void> scheduleMessage(IMessage<H> msg, long delay, TimeUnit unit);
}
