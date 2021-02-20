package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;

/***
 * 时间戳转换的面板
 *
 * @Author qiunet
 * @Date 2021/2/9 21:56
 **/
public class TimePanel extends IconJPanel {

	private JPanel showPanel;

	@Override
	public void initialize() {

	}

	@Override
	public void loadData() {

	}

	@Override
	public IconButtonType type(){
		return IconButtonType.time;
	}

	@Override
	public JPanel getShowPanel() {
		return showPanel;
	}

	@Override
	public void addToParent(ToolTabPanel toolTabPanel) {
		toolTabPanel.getPanelUp().add(this.getButton());
	}
}
