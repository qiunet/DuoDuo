package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.swing.IconButtonManager;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;
import java.awt.*;

/***
 * 工具切换面板
 *
 * @author qiunet
 * 2021-02-07 16:35
 */
public class ToolTabPanel extends JPanel {
	private JPanel panelUp;
	private JPanel panelDown;

    public ToolTabPanel() {
        this.initialize();
        this.addButton();
        this.addListener();
    }

    private void initialize(){
        Dimension size = new Dimension(UiConstant.TOOLS_PANEL_WIDTH, UiConstant.MAIN_WINDOW_HEIGHT);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);

        this.setBackground(UiConstant.TOOL_BAR_BACK_COLOR);
        this.setLayout(new GridLayout(2, 1));

		this.panelUp = new JPanel(true);
		panelUp.setBackground(UiConstant.TOOL_BAR_BACK_COLOR);
		panelUp.setLayout(new FlowLayout(0, 0, 10));

		this.panelDown = new JPanel(true);
		panelDown.setBackground(UiConstant.TOOL_BAR_BACK_COLOR);
		panelDown.setLayout(new BorderLayout());

		this.add(panelUp);
		this.add(panelDown);
    }

    private void addButton(){
		for (IconButtonType value : IconButtonType.values()) {
			IconButtonManager.instance.getIconPanel(value).addToParent(this);
		}
    }

    private void addListener(){
		IconButtonManager.instance.getIconPanels().forEach(IIconPanel::addListener);
    }

	public JPanel getPanelDown() {
		return panelDown;
	}

	public JPanel getPanelUp() {
		return panelUp;
	}
}
