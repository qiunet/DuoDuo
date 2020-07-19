package org.qiunet.excel2cfgs.appender;

import com.google.common.collect.Lists;
import org.qiunet.excel2cfgs.enums.OutPutType;
import org.qiunet.excel2cfgs.enums.RoleType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  qiunet.
 * 17/10/30
 */
public class AppenderAttachable {
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 选择的json xml  xd等append
	 */
	private List<IAppender> appenders = new LinkedList<>();
	/**
	 * 所有数据
	 */
	private List<List<AppenderData>> appenderDatas = Lists.newArrayList();
	/**
	 * 每一行的数据
	 */
	private List<AppenderData> rowDatas = Lists.newArrayList();
	/**
	 * 保存的数值字段名称
	 */
	private List<NameAppenderData> nameAppenderDatas = Lists.newArrayList();

	public AppenderAttachable (String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void addNameAppender(String name, OutPutType outPutType) {
		this.nameAppenderDatas.add(new NameAppenderData(name, outPutType));
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
		appenderDatas.add(rowDatas);
		rowDatas = Lists.newArrayList();
	}

	public void append(AppenderData appenderData) {
		this.rowDatas.add(appenderData);
	}

	public void sheetOver(String sheetName) {
		for (IAppender appender : appenders) {
			appender.sheetOver(sheetName, this);
		}
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
