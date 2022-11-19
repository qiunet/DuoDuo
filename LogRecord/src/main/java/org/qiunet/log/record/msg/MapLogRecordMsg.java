package org.qiunet.log.record.msg;

import com.google.common.collect.Maps;
import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.utils.exceptions.CustomException;

import java.util.Map;

/***
 * 存储Map类型的数据 日志基类.
 * map的数据到 {@link org.qiunet.log.record.logger.IRecordLogger} 处理
 *
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class MapLogRecordMsg<LogType extends Enum<LogType> & ILogRecordType<LogType>> extends LogRecordMsg<LogType> {
	/**
	 * string joiner 带分隔符
	 */
	private final Map<String, Object> data = Maps.newHashMap();

	protected MapLogRecordMsg(LogType eventLogType) {
		super(eventLogType);
	}

	@Override
	public void append(Object val) {
		throw new CustomException("Not Support");
	}

	@Override
	public void append(String key, Object val) {
		Object old = this.data.put(key, val);
		if (old != null) {
			throw new CustomException("key repeated");
		}
	}

	@Override
	protected Map<String, Object> getData0() {
		return data;
	}
}
