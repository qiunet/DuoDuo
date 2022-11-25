package org.qiunet.log.record.msg;

import org.qiunet.log.record.content.ILogContentGetter;
import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.log.record.logger.LogRecordManager;

import java.util.function.Consumer;

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
	 * 循环获取LogData
	 * @param consumer 消费者
	 */
	void forEachData(Consumer<LogRowData> consumer);
	/**
	 * 将日志转换为对应的日志数据
	 * @param getter 数据转换器
	 * @return D类型的数据
	 * @param <D> string map之类的
	 */
	default <D> D getLogContentData(ILogContentGetter<D> getter) {
		return getter.getData(this);
	}
	/**
	 * 发送日志
	 */
	default void send() {
		LogRecordManager.instance.sendLog(this);
	}
}
