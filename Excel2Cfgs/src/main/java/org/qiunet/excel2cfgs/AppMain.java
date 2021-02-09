package org.qiunet.excel2cfgs;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.listener.Excel2CfgServerStartListenerData;
import org.qiunet.excel2cfgs.swing.panel.ToolTabPanel;
import org.qiunet.listener.event.data.ServerShutdownEventData;
import org.qiunet.utils.logger.LoggerType;
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
    private JFrame frame;

    public static void main(String[] args) {
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

        this.mainPanelCenter = new JPanel(true);
        mainPanel.add(mainPanelCenter, BorderLayout.CENTER);
        return mainPanel;
    }

    public JPanel getMainPanelCenter() {
        return mainPanelCenter;
    }
}
