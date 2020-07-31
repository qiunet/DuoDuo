package org.qiunet.excel2cfgs.appender;

import org.qiunet.excel2cfgs.enums.OutPutType;

/***
 *
 * @author qiunet
 * 2020-07-19 10:03
 **/
class NameAppenderData {
	/***
	 * 数据名称
	 */
	protected String name;
	/***
	 * 输出类型
	 *
	 */
	protected OutPutType outPutType;

	public NameAppenderData(String name, OutPutType outPutType) {
		this.name = name;
		this.outPutType = outPutType;
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
