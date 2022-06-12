package org.qiunet.log.record.enums;
/***
 * 自己业务的枚举实现该接口.
 *
 * @author qiunet
 * 2020-03-25 09:52
 ***/
public interface ILogRecordType<T extends Enum<T> & ILogRecordType<T>> {
	/**
	 * 日志的文件名前缀.
	 * 后面会自动跟日期.
	 *
	 * @return
	 */
	default String getLogRecordName() {
		return ((T) this).name().toLowerCase();
	}
}
