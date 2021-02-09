package org.qiunet.excel2cfgs.swing.enums;

import org.qiunet.excel2cfgs.AppMain;
import org.qiunet.excel2cfgs.swing.IToolPanel;
import org.qiunet.excel2cfgs.swing.component.IconButton;
import org.qiunet.excel2cfgs.swing.panel.CfgPanel;
import org.qiunet.excel2cfgs.swing.panel.JsonFormatPanel;
import org.qiunet.excel2cfgs.swing.panel.SettingPanel;
import org.qiunet.excel2cfgs.swing.panel.TimePanel;

import javax.swing.*;

/***
 * 工具栏按钮
 *
 * @Author qiunet
 * @Date 2021/2/9 22:04
 **/
public enum ToolButtons {
	cfg(new CfgPanel(), "配置转换"),

	time(new TimePanel(), "时间戳转换"),

	json(new JsonFormatPanel(), "json格式化"),

	setting(new SettingPanel(),"设置"),
	;

	private final IToolPanel centerPanel;
	private final IconButton button;
	ToolButtons(IToolPanel centerPanel, String tip) {
		this.centerPanel = centerPanel;
		this.button = new IconButton(
				getImageIcon(this, ButtonStatus.normal),
				getImageIcon(this, ButtonStatus.enable),
				getImageIcon(this, ButtonStatus.disable),
				getImageIcon(this, ButtonStatus.rollover),
				tip);

	}

	public IconButton getButton() {
		return this.button;
	}

	/**
	 * 获得图标
	 * @param status
	 * @return
	 */
	private static ImageIcon getImageIcon(ToolButtons buttons, ButtonStatus status) {
		return new ImageIcon(AppMain.class.getResource("/icon/"+buttons.name() + "_" + status.name() +".png"));
	}

	public void addListener() {
		this.button.addActionListener(e -> {
			for (ToolButtons value : values()) {
				value.button.setEnabled(value != this);
				if (value == this) {
					value.button.setIcon(getImageIcon(value, ButtonStatus.enable));
				}else {
					value.button.setIcon(getImageIcon(value, ButtonStatus.normal));
				}
			}
			AppMain.instance.getMainPanelCenter().removeAll();
			centerPanel.reload();
			AppMain.instance.getMainPanelCenter().add((JPanel) centerPanel);
			AppMain.instance.getMainPanelCenter().updateUI();
		});
	}
}
