package org.qiunet.project.init.define.mybatis;

import java.util.ArrayList;
import java.util.List;

/***
 *
 *
 * qiunet
 * 2019-08-19 15:51
 ***/
public class MybatisConfigDefine {
	private String fileName = "mybatis-config.xml";
	private String baseDir = "src/main/resources/mybatis";

	private List<String> files = new ArrayList<>();

	public void addExtraFile(String extraFileName) {
		this.files.add(extraFileName);
	}

	public void addExtraFile(MybatisExtraDefine define) {
		this.addExtraFile(define.getFile());
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public List<String> getFiles() {
		return files;
	}
}
