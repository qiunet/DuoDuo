package org.qiunet.appender;

import org.qiunet.utils.DataType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qiunet.
 * 17/10/30
 */
public class AppenderAttachable implements Appender{
	private String fileName;
	private List<Appender> appenders = new LinkedList<>();
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
	public void addAppender(Appender appender) {
		this.appenders.add(appender);
	}

	@Override
	public void rowRecordOver() {
		for (Appender appender : appenders) {
			appender.rowRecordOver();
		}
	}

	@Override
	public void recordNum(int count) {
		for (Appender appender : appenders) {
			appender.recordNum(count);
		}
	}

	@Override
	public void append(DataType datatype, String name, String val, boolean cliFlag) {
		for (Appender appender : appenders) {
			appender.append(datatype, name, val, cliFlag);
		}
	}

	@Override
	public void sheetOver(String sheetName) {
		for (Appender appender : appenders) {
			appender.sheetOver(sheetName);
		}
	}

	@Override
	public void fileOver() {
		for (Appender appender : appenders) {
			appender.fileOver();
		}
	}
}
