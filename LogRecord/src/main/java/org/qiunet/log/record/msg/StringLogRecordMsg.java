package org.qiunet.log.record.msg;

import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.utils.string.StringUtil;

import java.util.StringJoiner;

/***
 *  * 存储String类型的数据 日志基类.
 *  * 类最终交给 {@link org.qiunet.log.record.logger.IRecordLogger} 处理
 *
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class StringLogRecordMsg<LogType extends Enum<LogType> & ILogRecordType<LogType>> extends LogRecordMsg<LogType, String> {
	/**
	 * string joiner 带分隔符
	 */
	private final StringJoiner sj;
	/**
	 * key val 带的分隔符
	 */
	private final String keyValDelimiter;
	protected StringLogRecordMsg(LogType eventLogType) {
		this(eventLogType, "|");
	}

	protected StringLogRecordMsg(LogType eventLogType, String delimiter) {
		this(eventLogType, delimiter, "=");
	}

	protected StringLogRecordMsg(LogType eventLogType, String delimiter, String keyValDelimiter) {
		super(eventLogType);
		this.sj = new StringJoiner(delimiter);
		this.keyValDelimiter = keyValDelimiter;
	}

	@Override
	public void append(String key, Object val) {
		if (key == null) {
			this.sj.add(String.valueOf(val));
			return;
		}

		if (StringUtil.isEmpty(keyValDelimiter)) {
			throw new NullPointerException("keyValDelimiter must specify a string for keyVal`s delimiter!");
		}

		this.sj.add(key + keyValDelimiter + val);
	}

	@Override
	protected String getData0() {
		return sj.toString();
	}
}
