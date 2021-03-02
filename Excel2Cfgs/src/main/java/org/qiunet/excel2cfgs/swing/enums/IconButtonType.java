package org.qiunet.excel2cfgs.swing.enums;

import org.qiunet.excel2cfgs.swing.panel.*;

/***
 * 工具栏按钮类型
 * @Author qiunet
 * @Date 2021/2/10 09:39
 **/
public enum IconButtonType {
	/**
	 * 设定转换
	 */
	cfg(new CfgPanel(), "Excel配置转换"),
	/**
	 * json
	 */
	json(new JsonFormatPanel(), "Json格式化"),
	/**
	 * 时间戳转换
	 */
	time(new TimePanel(), "时间戳转换"),
	/***
	 * 工具集合
	 */
	tools(new ToolsPanel(), "工具集合"),
	/**
	 * gm 命令调试发送
	 * 道具发送等
	 */
	gm(new GmPanel(), "GM命令"),
	/**
	 * 设置
	 */
	setting(new SettingPanel(), "设置"),
	;
	private final String title;
	private final IIconPanel panel;

	IconButtonType(IIconPanel panel, String title) {
		this.title = title;
		this.panel = panel;
	}

	public IIconPanel getPanel() {
		return panel;
	}

	public String getTitle() {
		return title;
	}
}
