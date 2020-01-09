package org.qiunet.appender;

import org.qiunet.frame.enums.DataType;
import org.qiunet.frame.enums.OutPutType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qiunet.
 * 17/10/30
 */
public class AppenderAttachable implements IAppender {
	private String fileName;
	private List<IAppender> appenders = new LinkedList<>();
	public AppenderAttachable (String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	/**
	 * 添加一个Appender
	 * @param appender
	 */
	public void addAppender(IAppender appender) {
		this.appenders.add(appender);
	}

	@Override
	public void rowRecordOver() {
		appenders.forEach(IAppender::rowRecordOver);
	}

	@Override
	public void recordNum(int count) {
		appenders.forEach(appender -> appender.recordNum(count));
	}

	@Override
	public void append(DataType datatype, String name, String val, OutPutType outPutType) {
		for (IAppender appender : appenders) {
			appender.append(datatype, name, val, outPutType);
		}
	}

	@Override
	public void sheetOver(String sheetName) {
		for (IAppender appender : appenders) {
			appender.sheetOver(sheetName);
		}
	}

	@Override
	public void fileOver() {
		appenders.forEach(IAppender::fileOver);
	}

	@Override
	public String name() {
		return null;
	}

	public int getAppenderSize(){
		return appenders.size();
	}
}
