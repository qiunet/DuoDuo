package org.qiunet.appender;


import org.qiunet.frame.enums.OutPutType;
import org.qiunet.frame.setting.SettingManager;
import org.qiunet.frame.enums.DataType;

import java.io.File;

/**
 * 一个文件的处理流程
 * @author  qiunet.
 * 17/10/30
 */
public interface IAppender {
	/***
	 * 行记录结束
	 */
	public void rowRecordOver();
	/***
	 * 记录行数
	 * @param count
	 */
	 void recordNum(int count);
	/***
	 * 拼一个数据
	 * @param datatype
	 * @param name
	 * @param val
	 */
	 void append(DataType datatype, String name , String val, OutPutType outPutType);

	/***
	 * 一个sheet结束
	 * @param sheetName
	 */
	 void sheetOver(String sheetName);

	/***
	 * 文件结束
	 */
	 void fileOver();

	/**
	 * 获得输出路径
	 * @return
	 */
	default String getServerOutputPath() {
		return SettingManager.getInstance().getFirstExcelPath()
			+ File.separator + "."
			+ this.name().toLowerCase() + ".config/server";
	}
	/**
	 * 获得输出路径
	 * @return
	 */
	default String getClientOutputPath() {
		return SettingManager.getInstance().getFirstExcelPath()
			+ File.separator + "."
			+ this.name().toLowerCase() + ".config/client";
	}

	/***
	 * append 名称
	 * @return
	 */
	String name();
}
