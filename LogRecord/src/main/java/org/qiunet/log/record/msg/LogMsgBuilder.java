package org.qiunet.log.record.msg;

import org.qiunet.utils.string.StringUtil;

import java.util.StringJoiner;

/***
 * Log 的消息拼接对象
 * @author qiunet
 * 2020-04-25 10:12
 **/
class LogMsgBuilder {
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
