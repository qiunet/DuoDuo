package org.qiunet.fx.bean;


import org.qiunet.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * created by wgw on 2019/9/7
 */
public class ConfigData {
	private String excelPath;
	private List<String> outPaths;//导出目录
	private boolean xml;
	private boolean json;
	private boolean xd;


	public ConfigData() {

	}

	public ConfigData(String excelPath){
		this.excelPath=excelPath;
	}


	public String getExcelPath() {
		return excelPath;
	}

	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}

	public List<String> getOutPaths() {
		return outPaths;
	}

	public void setOutPaths(List<String> outPaths) {
		this.outPaths = outPaths;
	}

	public boolean isXml() {
		return xml;
	}

	public void setXml(boolean xml) {
		this.xml = xml;
	}

	public boolean isJson() {
		return json;
	}

	public void setJson(boolean json) {
		this.json = json;
	}

	public boolean isXd() {
		return xd;
	}

	public void setXd(boolean xd) {
		this.xd = xd;
	}

	public String getFirstOutPath(){
		if(outPaths==null || outPaths.isEmpty()) return null;
		return outPaths.get(0);
	}

	public void addOutPath(String outPath) {
		if (StringUtil.isEmpty(outPath)) return;
		if (outPaths == null)
			outPaths = new ArrayList<>();
		if (outPaths.contains(outPath))
			outPaths.remove(outPath);
		outPaths.add(0,outPath);
	}

	@Override
	public int hashCode() {
		return getExcelPath().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof ConfigData)) return false;
		ConfigData tag = (ConfigData) obj;
		return getExcelPath().equals(tag.getExcelPath());
	}

}
