package org.qiunet.log.record.msg;

import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.log.record.logger.LogRecordManager;

/***
 *
 * 埋点日志的接口
 *
 * @author qiunet
 * 2022/11/18 18:15
 */
public interface ILogRecordMsg<LogType extends Enum<LogType> & ILogRecordType<LogType>> {
	/***
	 *  创建时间
	 */
	long createTime();

	/**
	 * 日志类型
	 * @return type
	 */
	LogType logType();

	/**
	 * 仅添加值
	 * @param val
	 */
	default void append(Object val) {
		this.append(null, val);
	}
	/**
	 * 往日志添加key val
	 * @param key 字段的key
	 * @param val 字段的val
	 * @return 本身
	 */
	void append(String key, Object val);
	/**
	 * 获得数据, 可以是编好的字符串. 也可以是其它. 比如map list什么的.
	 * @return
	 */
	Object getData();
	/**
	 * 发送日志
	 */
	default void send() {
		LogRecordManager.instance.sendLog(this);
	}
}
