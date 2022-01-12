package org.qiunet.excel2cfgs.appender;

import org.apache.commons.compress.utils.Sets;
import org.qiunet.excel2cfgs.common.enums.DataType;
import org.qiunet.excel2cfgs.common.enums.OutPutType;
import org.qiunet.excel2cfgs.common.enums.RoleType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author  qiunet.
 * 17/10/30
 */
public class AppenderAttachable {
	/**true 的字符串值**/
	private static final Set<String> TRUE_VAL_SET = Sets.newHashSet("yes", "1", "true");
	/**如果字段名为 该名. 该行数据忽略. */
	private static final String IGNORE_FIELD_NAME = ".ignore";
	/**
	 * 文件名
	 */
	private final String fileName;
	/**
	 * 选择的json xml  xd等append
	 */
	private final List<IAppender> appenders = new LinkedList<>();
	/**
	 * 所有数据
	 */
	private final List<List<AppenderData>> appenderDatas = new ArrayList<>();
	/**
	 * 每一行的数据
	 */
	private List<AppenderData> rowDatas = new ArrayList<>();
	/**
	 * 保存的数值字段名称
	 */
	private final List<NameAppenderData> nameAppenderDatas = new ArrayList<>();
	/**
	 * 忽略的数据
	 */
	private boolean ignoreData;

	public AppenderAttachable (String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void addNameAppender(String desc, String name, DataType dataType, OutPutType outPutType) {
		this.nameAppenderDatas.add(new NameAppenderData(desc, name, dataType, outPutType));
	}

	/***
	 * 获得数据的名称
	 * xd 以后考虑写开始. 方便反射赋值
	 * @return
	 */
	public List<NameAppenderData> getRowNames(RoleType roleType) {
		return nameAppenderDatas.stream().filter(
			data -> data.getOutPutType().canWrite(roleType)
		).collect(Collectors.toList());
	}
	/**
	 * 添加一个Appender
	 * @param appender
	 */
	public void addAppender(IAppender appender) {
		this.appenders.add(appender);
	}

	public void rowRecordOver() {
		if (! ignoreData) {
			appenderDatas.add(rowDatas);
		}
		rowDatas = new ArrayList<>();
		ignoreData = false;
	}

	public void append(AppenderData appenderData) {
		if (appenderData.name.equals(IGNORE_FIELD_NAME) && TRUE_VAL_SET.contains(appenderData.getVal())) {
			ignoreData = true;
		}
		this.rowDatas.add(appenderData);
	}

	public void sheetOver(String sheetName) {
		for (IAppender appender : appenders) {
			appender.sheetOver(sheetName, this);
		}
		this.nameAppenderDatas.clear();
		this.appenderDatas.clear();
	}

	public List<List<AppenderData>> getAppenderDatas() {
		return appenderDatas;
	}

	public void fileOver() {
		appenders.forEach(IAppender::fileOver);
	}

	public int getAppenderSize(){
		return appenders.size();
	}
}
