package org.qiunet.appender;

import org.qiunet.frame.enums.DataType;
import org.qiunet.frame.enums.OutPutType;

import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-01-10 11:09
 ***/
public class AppenderData {
	/***
	 * 数据类型
	 */
	private DataType dataType;
	/***
	 * 数据名称
	 */
	private String name;
	/**
	 * 数据值
	 */
	private String val;
	/***
	 * 输出类型
	 *
	 */
	private OutPutType outPutType;

	public AppenderData(DataType dataType, String name, String val, OutPutType outPutType) {
		this.dataType = dataType;
		this.name = name;
		this.val = val;
		this.outPutType = outPutType;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getName() {
		return name;
	}

	public String getVal() {
		return val;
	}

	public OutPutType getOutPutType() {
		return outPutType;
	}

	@Override
	public String toString() {
		return "AppenderData{" +
			"dataType=" + dataType +
			", name='" + name + '\'' +
			", val='" + val + '\'' +
			", outPutType=" + outPutType +
			'}';
	}
}
