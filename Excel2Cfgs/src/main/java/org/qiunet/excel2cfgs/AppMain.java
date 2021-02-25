package org.qiunet.excel2cfgs;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.panel.ToolTabPanel;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private TrayIcon trayIcon;// 托盘图标
	private SystemTray systemTray;// 系统托盘
	private JPanel mainPanelCenter;
	private final TitledBorder border = new TitledBorder("=");


	public static void main(String[] args) {
        AppMain.instance.init();
    }

    private void handlerTray(JFrame frame) {
		if (! SystemTray.isSupported()) {
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			return;
		}

		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		systemTray = SystemTray.getSystemTray();// 获得系统托盘的实例
		trayIcon = new TrayIcon(UiConstant.IMAGE_ICON);
		PopupMenu popupMenu = new PopupMenu();
		MenuItem exit = new MenuItem("Exit");
		exit.setFont(UiConstant.DEFAULT_FONT);
		exit.addActionListener(e -> System.exit(0));
		popupMenu.add(exit);
		trayIcon.setPopupMenu(popupMenu);
		//为图标设置鼠标监听器
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1) {
					return;
				}
				frame.setExtendedState(Frame.NORMAL);
				frame.setVisible(true);
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// 设置托盘的图标
				try {
					systemTray.add(trayIcon);
				} catch (AWTException ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void windowActivated(WindowEvent e) {
				systemTray.remove(trayIcon);

			}
		});
	}

    private void init(){
		JFrame frame = new JFrame(UiConstant.MAIN_WINDOWS_TITLE);
        frame.setBackground(UiConstant.MAIN_BACK_COLOR);
        frame.setIconImage(UiConstant.IMAGE_ICON);
        frame.setSize(UiConstant.MAIN_SIZE);
        this.handlerTray(frame);

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

        this.mainPanelCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        this.border.setTitleFont(new Font("微软雅黑", Font.BOLD, 32));
        this.border.setTitleColor(UiConstant.TOOL_BAR_BACK_COLOR);
        this.mainPanelCenter.setBorder(border);
		mainPanel.add(mainPanelCenter, BorderLayout.CENTER);
        return mainPanel;
    }

	public TitledBorder getBorder() {
		return border;
	}

	public JPanel getMainPanelCenter() {
        return mainPanelCenter;
    }

	public JPanel getStatusPanel() {
		return mainPanelCenter;
	}
}
