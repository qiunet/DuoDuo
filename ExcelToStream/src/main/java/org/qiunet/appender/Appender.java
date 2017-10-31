package org.qiunet.appender;


import org.qiunet.utils.DataType;

/**
 * 一个文件的处理流程
 * Created by qiunet.
 * 17/10/30
 */
public interface Appender {
	/***
	 * 行记录结束
	 */
	public void rowRecordOver();
	/***
	 * 记录行数
	 * @param count
	 */
	public void recordNum(int count);
	/***
	 * 拼一个数据
	 * @param datatype
	 * @param name
	 * @param val
	 * @param cliFlag 客户端是否记录的flag true 为需要,
	 */
	public void append(DataType datatype, String name , String val, boolean cliFlag);

	/***
	 * 一个sheet结束
	 * @param sheetName
	 */
	public void sheetOver(String sheetName);

	/***
	 * 文件结束
	 */
	public void fileOver();
}
