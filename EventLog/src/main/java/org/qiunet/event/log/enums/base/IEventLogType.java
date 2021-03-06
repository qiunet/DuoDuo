package org.qiunet.event.log.enums.base;

import org.qiunet.event.log.enums.RecordModel;

/***
 * 自己业务的枚举实现该接口.
 *
 * @author qiunet
 * 2020-03-25 09:52
 ***/
public interface IEventLogType {
	/**
	 * 日志的文件名前缀.
	 * 后面会自动跟日期.
	 *
	 * @return
	 */
	default String getLoggerName() {
		return name().toLowerCase();
	}
	/**
	 * 如果是枚举. 不用实现该方法
	 * @return
	 */
	String name();
	/**
	 * 保存方式
	 * @return
	 */
	default RecordModel recordModel(){
		return RecordModel.LOCAL;
	}
}
