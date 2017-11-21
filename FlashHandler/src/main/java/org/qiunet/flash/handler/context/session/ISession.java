package org.qiunet.flash.handler.context.session;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.Channel;

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
	 * @param obj
	 */
	void write(GeneratedMessageV3 obj);
}
