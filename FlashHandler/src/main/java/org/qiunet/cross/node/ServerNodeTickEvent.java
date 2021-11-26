package org.qiunet.cross.node;

import org.qiunet.utils.listener.event.IEventData;

/***
 * 服务器节点的心跳.
 * 每分钟一次 不要有太复杂的逻辑.
 *
 * @author qiunet
 * 2021/11/3 10:47
 */
public enum ServerNodeTickEvent implements IEventData {
	instance
}
