package org.qiunet.log.record.content;

import org.qiunet.log.record.msg.ILogRecordMsg;

/***
 * 获取日志的内容最终数据
 * 业务确定了后, 可以用常量管理实例
 * @author qiunet
 * 2022/11/25 10:46
 */
public interface ILogContentGetter<D> {
	/**
	 * map类型的getter
	 */
	MapLogContentGetter MAP_LOG_CONTENT_GETTER = new MapLogContentGetter();
	/**
	 * 获得日志的内容
	 * @param msg 日志的消息
	 * @return D
	 */
	D getData(ILogRecordMsg<?> msg);
}
