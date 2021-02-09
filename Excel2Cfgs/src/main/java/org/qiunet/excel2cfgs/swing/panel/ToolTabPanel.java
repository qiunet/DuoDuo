package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.AppMain;
import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.swing.component.IconButton;

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
        panelUp.add(Buttons.cfg.getButton());
        panelUp.add(Buttons.json.getButton());
        panelUp.add(Buttons.time.getButton());

        JPanel panelDown = new JPanel(true);
        panelDown.setBackground(UiConstant.TOOL_BAR_BACK_COLOR);
        panelDown.setLayout(new BorderLayout());
        panelDown.add(Buttons.setting.getButton(), BorderLayout.SOUTH);

        this.add(panelUp);
        this.add(panelDown);
    }

    private void addListener(){
        for (Buttons value : Buttons.values()) {
            value.addListener();
        }
    }

    /**
     * 按钮状态
     */
    enum ButtonStatus {disable, normal, enable, rollover}

    /**
     * 按钮
     */
    private enum Buttons {
        cfg(null, "配置转换"),

        time(null, "时间戳转换"),

        json(null, "json格式化"),

        setting(new SettingPanel(),"设置"),
        ;
        private JPanel centerPanel;
        private final IconButton button;
        private final String tip;

        Buttons(JPanel centerPanel, String tip) {
            this.tip = tip;
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
        private static ImageIcon getImageIcon(Buttons buttons, ButtonStatus status) {
            return new ImageIcon(AppMain.class.getResource("/icon/"+buttons.name() + "_" + status.name() +".png"));
        }

        public void addListener() {
            this.button.addActionListener(e -> {
                for (Buttons value : Buttons.values()) {
					value.button.setEnabled(value != this);
					if (value == this) {
						value.button.setIcon(getImageIcon(value, ButtonStatus.enable));
                    }else {
                        value.button.setIcon(getImageIcon(value, ButtonStatus.normal));
                    }
                }
                AppMain.instance.getMainPanelCenter().removeAll();

                AppMain.instance.getMainPanelCenter().updateUI();
            });
        }
    }
}
