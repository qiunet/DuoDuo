package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

/***
 * 时间戳转换的面板
 *
 * @Author qiunet
 * @Date 2021/2/9 21:56
 **/
public class TimePanel extends IconJPanel {

	@Override
	public void initialize() {
//		this.add(new TimeForm().getMainPanel());
	}

	@Override
	public void loadData() {

	}

	@Override
	public IconButtonType type(){
		return IconButtonType.time;
	}

	@Override
	public void addToParent(ToolTabPanel toolTabPanel) {
		toolTabPanel.getPanelUp().add(this.getButton());
	}
}
