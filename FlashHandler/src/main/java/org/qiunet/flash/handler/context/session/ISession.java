package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.context.response.push.IMessage;

import java.util.concurrent.TimeUnit;

/**
 * session 的接口,
 * Created by qiunet.
 * 17/10/23
 */
public interface ISession {
	/***
	 * 得到session的队列索引
	 * 默认为 channel的id  其它情况: 比如房间id 什么. 可以消除房间的并发等.
	 * @return
	 */
	int getQueueIndex();
	/***
	 * 重置队列索引.
	 * @return
	 */
	void resetQueueIndex();
	/***
	 * 设置队列索引. 因为可能进入房间了. 所有人的队列索引都设置为一样
	 * @param queueIndex
	 * @return
	 */
	void setQueueIndex(int queueIndex);
	/***
	 *
	 * @return
	 */
	Channel getChannel();

	/***
	 * 得到存放map的唯一key
	 * @return
	 */
	long getUid();
	/**
	 * 调用sessionClose后的逻辑.
	 */
	void fireSessionClose();
	/***
	 * 对外写消息
	 * @param message
	 */
	ChannelFuture writeMessage(IMessage message);

	/***
	 * 添加待处理事情
	 */
	void addProcessMessage(IProcessMessage msg);

	/***
	 * 添加待处理事情 延迟处理
	 */
	void addProcessMessage(IProcessMessage msg, long delay, TimeUnit unit);
	/***
	 * 添加待处理事情 指定时间执行
	 * @param timeMillis 时间戳
	 */
	void addProcessMessage(IProcessMessage msg, long timeMillis);
}
