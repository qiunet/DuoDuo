package org.qiunet.appender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.qiunet.frame.enums.DataType;
import org.qiunet.frame.enums.OutPutType;

import java.util.List;

/***
 *
 *
 * qiunet
 * 2019-11-25 15:20
 ***/
public class XmlAppender implements IAppender {
	private String filePrefix;
	/**
	 * 相对根目录的目录相对路径
	 */
	private String relativeDirPath;

	public XmlAppender(String filePrefix, String relativeDirPath) {
		this.filePrefix = filePrefix;
		this.relativeDirPath = relativeDirPath;
	}

	@Override
	public void createCfgFile(String sheetName, boolean server, String outPath, AppenderAttachable attachable) {

	}

	@Override
	public void fileOver() {

	}

	@Override
	public String name() {
		return "xml";
	}
}
