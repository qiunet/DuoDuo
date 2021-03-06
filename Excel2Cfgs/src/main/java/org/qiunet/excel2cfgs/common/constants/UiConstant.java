package org.qiunet.excel2cfgs.common.constants;

import org.qiunet.excel2cfgs.AppMain;

import java.awt.*;

/***
 *
 *
 * @author qiunet
 * 2021-02-07 15:27
 */
public interface UiConstant {
    /**
     * 主窗口 宽度
     */
    int MAIN_WINDOW_WIDTH = 1053;
    /**
     * 主窗口 高度
     */
    int MAIN_WINDOW_HEIGHT = 720;
	/**
	 *  工具面板宽度
	 */
	int TOOLS_PANEL_WIDTH = 58;
	/**
	 * 文件选择器大小
	 */
	Dimension FILE_CHOOSER_SIZE = new Dimension(700, 500);

    /**
     * 主窗口图标
     */
    Image IMAGE_ICON = Toolkit.getDefaultToolkit()
            .getImage(AppMain.class.getResource("/icon/ico.png"));
    /**
     * 主窗口大小
     */
    Dimension MAIN_SIZE = new Dimension(MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT);
    /**
     * 主窗口背景色
     */
    Color MAIN_BACK_COLOR = Color.WHITE;
    /**
     * 工具栏背景色
     */
    Color TOOL_BAR_BACK_COLOR = new Color(37, 174, 96);
	/**
	 * 默认字体
	 */
    Font DEFAULT_FONT = new Font("微软雅黑", Font.BOLD, 18);
}
