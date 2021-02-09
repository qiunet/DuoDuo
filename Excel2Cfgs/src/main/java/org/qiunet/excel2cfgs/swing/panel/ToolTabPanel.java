package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.swing.enums.ToolButtons;

import javax.swing.*;
import java.awt.*;

/***
 * 工具切换面板
 *
 * @author qiunet
 * 2021-02-07 16:35
 */
public class ToolTabPanel extends JPanel {

    public ToolTabPanel() {
        this.initialize();
        this.addButton();
        this.addListener();
    }

    private void initialize(){
        Dimension size = new Dimension(58, UiConstant.MAIN_WINDOW_HEIGHT);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);

        this.setBackground(UiConstant.TOOL_BAR_BACK_COLOR);
        this.setLayout(new GridLayout(2, 1));
    }

    private void addButton(){
        JPanel panelUp = new JPanel(true);
        panelUp.setBackground(UiConstant.TOOL_BAR_BACK_COLOR);
        panelUp.setLayout(new FlowLayout(0, 0, 10));
        panelUp.add(ToolButtons.cfg.getButton());
        panelUp.add(ToolButtons.json.getButton());
        panelUp.add(ToolButtons.time.getButton());

        JPanel panelDown = new JPanel(true);
        panelDown.setBackground(UiConstant.TOOL_BAR_BACK_COLOR);
        panelDown.setLayout(new BorderLayout());
        panelDown.add(ToolButtons.setting.getButton(), BorderLayout.SOUTH);

        this.add(panelUp);
        this.add(panelDown);
    }

    private void addListener(){
        for (ToolButtons value : ToolButtons.values()) {
            value.addListener();
        }
    }
}
