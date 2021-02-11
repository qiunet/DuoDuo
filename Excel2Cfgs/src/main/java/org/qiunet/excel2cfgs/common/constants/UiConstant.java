package org.qiunet.excel2cfgs.common.constants;

import org.qiunet.excel2cfgs.AppMain;
import org.qiunet.utils.system.SystemPropertyUtil;

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
    int MAIN_WINDOW_HEIGHT = 680;
	/**
	 * 选择框大小
	 */
	Dimension COMBO_BOX_SIZE = new Dimension(600, 40);

    String MAIN_WINDOWS_TITLE = "DuoDuo Tools";
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
     * 表格线条背景色
     */
    Color TABLE_LINE_COLOR = new Color(229, 229, 229);

    // 字体
    /**
     * 标题字体
     */
    Font FONT_TITLE = new Font(SystemPropertyUtil.get("ds.ui.font.family"), 0, 27);
    /**
     * 普通字体
     */
    Font FONT_NORMAL = new Font(SystemPropertyUtil.get("ds.ui.font.family"), 0, 13);
    /**
     * radio字体
     */
    Font FONT_RADIO = new Font(SystemPropertyUtil.get("ds.ui.font.family"), 0, 15);


}
