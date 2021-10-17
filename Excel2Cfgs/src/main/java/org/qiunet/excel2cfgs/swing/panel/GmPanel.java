package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;

/***
 *
 *
 * @author qiunet
 * 2021-02-24 15:56
 */
public class GmPanel extends IconJPanel {
	private JPanel showPanel;

	@Override
	public void activate() {

	}

	@Override
	public void unActivate() {

	}

	@Override
	public void addToParent(ToolTabPanel toolTabPanel) {
		toolTabPanel.getPanelUp().add(getButton());
	}

	@Override
	public IconButtonType type() {
		return IconButtonType.gm;
	}

	@Override
	public JPanel getShowPanel() {
		return showPanel;
	}
}
