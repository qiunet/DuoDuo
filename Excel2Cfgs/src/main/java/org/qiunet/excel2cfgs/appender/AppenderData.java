package org.qiunet.excel2cfgs.appender;

import org.qiunet.excel2cfgs.enums.OutPutType;


/***
 *
 *
 * @author qiunet
 * 2020-01-10 11:09
 ***/
public class AppenderData extends NameAppenderData{
	/**
	 * 值
	 */
	private String val;

	public AppenderData(String name, String val, OutPutType outPutType) {
		super(name, outPutType);
		this.val = val;
	}

	public String getVal() {
		return val;
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
