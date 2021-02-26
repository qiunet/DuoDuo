package org.qiunet.excel2cfgs.setting;

import org.qiunet.excel2cfgs.common.enums.RoleType;
import org.qiunet.excel2cfgs.common.utils.Excel2CfgsUtil;

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
	/***
	 * 角色类型
	 * 服务端 客户端 和 策划
	 * 服务端 客户端输出到项目配置路径
	 * 策划输出到根目录下.
	 */
	private RoleType roleType = RoleType.SERVER;

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

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public void update(){
		Excel2CfgsUtil.writeToProjectFile(this);
	}
}
