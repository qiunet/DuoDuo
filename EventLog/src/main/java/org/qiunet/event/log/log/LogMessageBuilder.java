package org.qiunet.event.log.log;

import org.qiunet.utils.string.StringUtil;

import java.util.StringJoiner;

/***
 *
 * @author qiunet
 * 2020-04-25 10:12
 **/
public class LogMessageBuilder {
	private StringJoiner sj;
	private String keyValDelimiter;
	/**
	 * log的数据间隔符
	 * 生成格式例如:  val1|val2|val3
	 * @param delimiter
	 */
	public LogMessageBuilder(String delimiter) {
		sj = new StringJoiner(delimiter);
	}

	/**
	 * 会有key val的一个生成格式
	 * 生成格式: key1=val1|key2=val2
	 * @param delimiter 一组数据的间隔符号.
	 * @param keyValDelimiter key val的间隔符号
	 */
	public LogMessageBuilder(String delimiter, String keyValDelimiter) {
		sj = new StringJoiner(delimiter);
		this.keyValDelimiter = keyValDelimiter;
	}

	public LogMessageBuilder add(Object val) {
		this.sj.add(String.valueOf(val));
		return this;
	}

	public LogMessageBuilder add(Object key, Object val) {
		if (StringUtil.isEmpty(keyValDelimiter)) {
			throw new NullPointerException("keyValDelimiter must specify a string for keyVal`s delimiter!");
		}
		this.sj.add(key + keyValDelimiter + val);
		return this;
	}

	@Override
	public String toString() {
		return sj.toString();
	}
}
