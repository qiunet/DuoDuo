package org.qiunet.excel2cfgs.appender;

import org.qiunet.excel2cfgs.common.enums.DataType;
import org.qiunet.excel2cfgs.common.enums.OutPutType;

/***
 *
 * @author qiunet
 * 2020-07-19 10:03
 **/
class NameAppenderData {
	protected String desc;
	/***
	 * 数据名称
	 */
	protected String name;
	/**
	 * 数据类型
	 */
	protected DataType dataType;
	/***
	 * 输出类型
	 *
	 */
	protected OutPutType outPutType;

	public NameAppenderData(String desc, String name, DataType dataType, OutPutType outPutType) {
		this.name = name;
		this.desc = desc;
		this.dataType = dataType;
		this.outPutType = outPutType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OutPutType getOutPutType() {
		return outPutType;
	}

	public void setOutPutType(OutPutType outPutType) {
		this.outPutType = outPutType;
	}
}
