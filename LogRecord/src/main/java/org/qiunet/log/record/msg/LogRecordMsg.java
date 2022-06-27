package org.qiunet.log.record.msg;

import org.qiunet.log.record.enums.ILogRecordType;
import org.qiunet.log.record.logger.LogRecordManager;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.string.StringUtil;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * 日志的基类.
 *
 * @author qiunet
 * 2020-03-30 07:52
 **/
public abstract class LogRecordMsg<LogType extends Enum<LogType> & ILogRecordType<LogType>> {
	private final AtomicBoolean logged = new AtomicBoolean();
	private final LogMsgBuilder logMsgBuilder;
	protected final LogType eventLogType;
	protected final long createTime;

	protected LogRecordMsg(LogType eventLogType) {
		this(eventLogType, "|");
	}

	protected LogRecordMsg(LogType eventLogType, String delimiter) {
		this(eventLogType, delimiter, "=");
	}

	protected LogRecordMsg(LogType eventLogType, String delimiter, String keyValDelimiter) {
		this.logMsgBuilder = new LogMsgBuilder(delimiter, keyValDelimiter);
		this.createTime = System.currentTimeMillis();
		this.eventLogType = eventLogType;
	}

	public String logMessage() {
		if (! logged.compareAndSet(false, true)) {
			throw new CustomException("Already output message!");
		}

		this.fillLogRecordMsg();
		return logMsgBuilder.toString();
	}

	protected void appendVal(Object val) {
		this.logMsgBuilder.add(val);
	}

	protected void appendVal(Object key, Object val) {
		this.logMsgBuilder.add(key, val);
	}

	/**
	 * 填充日志
	 */
	protected abstract void fillLogRecordMsg();

	public long createTime() {
		return createTime;
	}

	public LogType logType() {
		return eventLogType;
	}

	public void send() {
		LogRecordManager.instance.sendLog(this);
	}


	/***
	 * Log 的消息拼接对象
	 * @author qiunet
	 * 2020-04-25 10:12
	 **/
	private static class LogMsgBuilder {
		/**
		 * string joiner 带分隔符
		 */
		private final StringJoiner sj;
		/**
		 * key val 带的分隔符
		 */
		private final String keyValDelimiter;

		/**
		 * 会有key val的一个生成格式
		 * 生成格式: key1=val1|key2=val2  或者   val1|val2|val3
		 * @param delimiter 一组数据的间隔符号.
		 * @param keyValDelimiter key val的间隔符号
		 */
		LogMsgBuilder(String delimiter, String keyValDelimiter) {
			sj = new StringJoiner(delimiter);
			this.keyValDelimiter = keyValDelimiter;
		}

		/**
		 * 添加一个值
		 * @param val
		 * @return
		 */
		LogMsgBuilder add(Object val) {
			this.sj.add(String.valueOf(val));
			return this;
		}
		/**
		 * 添加一个key val 值
		 * @param val
		 * @return
		 */
		LogMsgBuilder add(Object key, Object val) {
			if (StringUtil.isEmpty(keyValDelimiter)) {
				throw new NullPointerException("keyValDelimiter must specify a string for keyVal`s delimiter!");
			}
			return this.add(key + keyValDelimiter + val);
		}

		@Override
		public String toString() {
			return sj.toString();
		}
	}
}
