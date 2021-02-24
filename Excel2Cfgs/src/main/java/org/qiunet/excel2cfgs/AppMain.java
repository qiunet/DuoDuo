package org.qiunet.excel2cfgs;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.panel.ToolTabPanel;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.scanner.ClassScanner;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/***
 *
 *
 * @author qiunet
 * 2021-02-07 15:13
 */
public enum AppMain {
    instance;

    private final Logger logger = LoggerType.DUODUO.getLogger();
	private JPanel mainPanelCenter;
	private JLabel titleLabel;

	public static void main(String[] args) {
		ClassScanner.getInstance().scanner();
        AppMain.instance.init();
    }

    private void init(){
		JFrame frame = new JFrame(UiConstant.MAIN_WINDOWS_TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(UiConstant.MAIN_BACK_COLOR);
        frame.setIconImage(UiConstant.IMAGE_ICON);
        frame.setSize(UiConstant.MAIN_SIZE);

        JPanel mainPanel = this.createMainPanel();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
				SettingManager.getInstance().update();
            }

			@Override
			public void windowOpened(WindowEvent e) {
				IconButtonType.cfg.getPanel().getButton().doClick();
			}
        });
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * 创建主面板
     * @return
     */
    private JPanel createMainPanel(){
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout());
		ToolTabPanel toolTabPanel = new ToolTabPanel();
        mainPanel.add(toolTabPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(true);
        contentPanel.setLayout(new BorderLayout());
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));

		this.titleLabel = new JLabel();
		this.titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 30));
		this.titleLabel.setForeground(UiConstant.TOOL_BAR_BACK_COLOR);
		titlePanel.add(this.titleLabel);
		contentPanel.add(titlePanel, BorderLayout.NORTH);


        this.mainPanelCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
		contentPanel.add(mainPanelCenter, BorderLayout.CENTER);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
        return mainPanel;
    }

	public JLabel getTitleLabel() {
		return titleLabel;
	}

	public JPanel getMainPanelCenter() {
        return mainPanelCenter;
    }

	public JPanel getStatusPanel() {
		return mainPanelCenter;
	}
}
