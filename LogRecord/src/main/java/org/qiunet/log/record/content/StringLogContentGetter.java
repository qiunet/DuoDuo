package org.qiunet.log.record.content;

import org.qiunet.log.record.msg.ILogRecordMsg;

import java.util.StringJoiner;

/***
 * 获取 string类型 日志内容
 * 业务确定了后, 可以用常量管理实例
 *
 * @author qiunet
 * 2022/11/25 10:55
 */
public class StringLogContentGetter implements ILogContentGetter<String> {
	/**
	 * key val 带的分隔符
	 */
	private final String keyValDelimiter;
	/**
	 * 数据中间的分割线
	 */
	private final String delimiter;

	/***
	 * @param keyValDelimiter key val 分割符号
	 * @param delimiter 一组key  val的分割符号
	 */
	public StringLogContentGetter(String keyValDelimiter, String delimiter) {
		this.keyValDelimiter = keyValDelimiter;
		this.delimiter = delimiter;
	}

	@Override
	public String getData(ILogRecordMsg<?> msg) {
		StringJoiner sj = new StringJoiner(delimiter);
		msg.forEachData(row -> {
			if (row.getKey() == null) {
				sj.add(String.valueOf(row.getVal()));
			}else {
				sj.add(row.getKey() + keyValDelimiter + row.getVal());
			}
		});
		return sj.toString();
	}
}
