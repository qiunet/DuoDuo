package org.qiunet.excel2cfgs;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.listener.Excel2CfgServerStartListenerData;
import org.qiunet.excel2cfgs.swing.panel.ToolTabPanel;
import org.qiunet.listener.event.data.ServerShutdownEventData;
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
    private ToolTabPanel toolTabPanel;
    private JPanel mainPanelCenter;
	private JLabel titleLabel;
	private JFrame frame;

    public static void main(String[] args) {
		ClassScanner.getInstance().scanner();
        AppMain.instance.init();
    }

    private void init(){
        this.frame = new JFrame(UiConstant.MAIN_WINDOWS_TITLE);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setBackground(UiConstant.MAIN_BACK_COLOR);
        this.frame.setIconImage(UiConstant.IMAGE_ICON);
        this.frame.setSize(UiConstant.MAIN_SIZE);

        JPanel mainPanel = this.createMainPanel();
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
				ServerShutdownEventData.fireShutdownEventHandler();
            }
            @Override
            public void windowActivated(WindowEvent e) {
                Excel2CfgServerStartListenerData.fireStartEventHandler();
            }
        });
        this.frame.add(mainPanel);
        this.frame.setVisible(true);
    }

    /**
     * 创建主面板
     * @return
     */
    private JPanel createMainPanel(){
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout());
        this.toolTabPanel = new ToolTabPanel();
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
}
