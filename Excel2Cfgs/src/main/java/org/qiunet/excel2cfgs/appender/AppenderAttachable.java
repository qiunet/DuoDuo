package org.qiunet.excel2cfgs.appender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  qiunet.
 * 17/10/30
 */
public class AppenderAttachable {
	private String fileName;
	private List<IAppender> appenders = new LinkedList<>();
	private List<List<AppenderData>> appenderDatas = new ArrayList<>();
	private List<AppenderData> rowDatas = new ArrayList<>();

	public AppenderAttachable (String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	/***
	 * 获得数据的名称
	 * xd 以后考虑写开始. 方便反射赋值
	 * @return
	 */
	public List<String> getRowNames() {
		if (appenderDatas.isEmpty())  {
			return Collections.emptyList();
		}

		return appenderDatas.get(0).stream()
			.map(AppenderData::getName)
			.collect(Collectors.toList());
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
		rowDatas = new ArrayList<>();
	}

	public void append(AppenderData appenderData) {
		this.rowDatas.add(appenderData);
	}

	public void sheetOver(String sheetName) {
		for (IAppender appender : appenders) {
			appender.sheetOver(sheetName, this);
		}
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
