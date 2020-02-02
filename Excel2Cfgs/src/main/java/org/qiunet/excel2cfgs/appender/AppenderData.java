package org.qiunet.excel2cfgs.appender;

import org.qiunet.excel2cfgs.frame.enums.OutPutType;


/***
 *
 *
 * @author qiunet
 * 2020-01-10 11:09
 ***/
public class AppenderData {
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

	public AppenderData(String name, String val, OutPutType outPutType) {
		this.name = name;
		this.val = val;
		this.outPutType = outPutType;
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
			", name='" + name + '\'' +
			", val='" + val + '\'' +
			", outPutType=" + outPutType +
			'}';
	}
}
