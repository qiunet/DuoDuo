package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;

/***
 * 配置转换的面板
 *
 * @Author qiunet
 * @Date 2021/2/9 21:56
 **/
public class CfgPanel extends IconJPanel {

	private JPanel showPanel;

	@Override
	public void initialize() {

	}

	@Override
	public void loadData() {
	}

	@Override
	public IconButtonType type(){
		return IconButtonType.cfg;
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
