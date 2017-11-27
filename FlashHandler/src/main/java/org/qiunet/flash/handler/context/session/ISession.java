package org.qiunet.flash.handler.context.session;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.header.MessageContent;

/**
 * session 的接口,
 * Created by qiunet.
 * 17/10/23
 */
public interface ISession<Key> {
	/***
	 * 得到session的队列索引
	 * 默认为 channel的id  其它情况: 比如房间id 什么. 可以消除房间的并发等.
	 * @return
	 */
	int getQueueIndex();

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
	Key getKey();
	/***
	 * push 一个消息
	 * @param content
	 */
	void write(MessageContent content);

	/**
	 * 最后活跃的时间戳
	 * @return
	 */
	long lastPackageTimeStamp();

	/***
	 * 最后的活跃时间
	 */
	void setLastPackageTimeStamp();
}
