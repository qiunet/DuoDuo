package org.qiunet.log.record.msg;

import com.google.common.collect.Lists;
import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.utils.exceptions.CustomException;

import java.util.List;
import java.util.function.Consumer;

/***
 * 日志的基类.
 *
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class LogRecordMsg<LogType extends Enum<LogType> & ILogRecordType<LogType>> implements ILogRecordMsg<LogType>{
	private final List<LogRowData> dataList = Lists.newLinkedList();
	protected final LogType eventLogType;
	protected final long createTime;
	private boolean appendStatus;

	protected LogRecordMsg(LogType eventLogType) {
		this.createTime = System.currentTimeMillis();
		this.eventLogType = eventLogType;
	}

	@Override
	public void forEachData(Consumer<LogRowData> consumer) {
		if (! appendStatus) {
			this.fillLogRecordMsg();
			appendStatus = true;
		}
		this.dataList.forEach(consumer);
	}

	@Override
	public void append(String key, Object val) {
		if (appendStatus) {
			throw new CustomException("Already output message!");
		}
		this.dataList.add(LogRowData.valueOf(key, val));
	}

	/**
	 * 填充日志
	 */
	protected abstract void fillLogRecordMsg();
	@Override
	public long createTime() {
		return createTime;
	}
	@Override
	public LogType logType() {
		return eventLogType;
	}
}
