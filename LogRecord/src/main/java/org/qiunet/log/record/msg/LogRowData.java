package org.qiunet.log.record.msg;

/***
 *
 * @author qiunet
 * 2022/11/25 11:13
 */
public class LogRowData {
	/**
	 * key
	 */
	private String key;
	/**
	 * val
	 */
	private Object val;

	public static LogRowData valueOf(String key, Object val){
		LogRowData data = new LogRowData();
	    data.key = key;
		data.val = val;
		return data;
	}

	public String getKey() {
		return key;
	}

	public Object getVal() {
		return val;
	}
}
