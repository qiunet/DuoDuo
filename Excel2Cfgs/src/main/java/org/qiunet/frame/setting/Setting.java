package org.qiunet.frame.setting;

import java.util.LinkedList;
import java.util.List;

/***
 *
 *
 * qiunet
 * 2019-11-06 12:09
 ***/
public class Setting {
	/***
	 * excel 的路径
	 */
	private LinkedList<String> excelPaths = new LinkedList<>();
	/***
	 * 配置往项目输出路径
	 */
	private LinkedList<String> cfgPaths = new LinkedList<>();
	/***
	 * xml是否勾选
	 */
	private boolean xmlChecked;
	/***
	 * xd是否勾选
	 */
	private boolean xdChecked;
	/***
	 * json是否勾选
	 */
	private boolean jsonChecked;

	public LinkedList<String> getExcelPaths() {
		return excelPaths;
	}

	public void setExcelPaths(List<String> excelPaths) {
		this.excelPaths = new LinkedList<>(excelPaths);
	}

	public LinkedList<String> getCfgPaths() {
		return cfgPaths;
	}

	public void setCfgPaths(List<String> cfgPaths) {
		this.cfgPaths = new LinkedList<>(cfgPaths);
	}

	public boolean isXmlChecked() {
		return xmlChecked;
	}

	public void setXmlChecked(boolean xmlChecked) {
		this.xmlChecked = xmlChecked;
	}

	public boolean isXdChecked() {
		return xdChecked;
	}

	public void setXdChecked(boolean xdChecked) {
		this.xdChecked = xdChecked;
	}

	public boolean isJsonChecked() {
		return jsonChecked;
	}

	public void setJsonChecked(boolean jsonChecked) {
		this.jsonChecked = jsonChecked;
	}
}
