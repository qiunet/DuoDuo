package org.qiunet.excel2cfgs.swing.component;

import javax.swing.*;
import java.awt.*;

/**
 * 自定义按钮类，支持自定义默认图标、激活图标、失效图标和tip提示
 *
 * @author Bob
 */
public class IconButton extends JButton {

    private static final long serialVersionUID = 1L;
    private final ImageIcon iconEnable;
	private final ImageIcon iconDisable;
	private final ImageIcon iconRollOver;
    private final String tip;

    /**
     * 构造
     *
     * @param iconNormal  默认图标
     * @param iconEnable  激活图标
     * @param iconDisable 失效图标
     * @param iconRollover 划过图标
     * @param tip         提示
     */
    public IconButton(ImageIcon iconNormal, ImageIcon iconEnable, ImageIcon iconDisable, ImageIcon iconRollover, String tip) {
        super(iconNormal);

        this.iconEnable = iconEnable;
        this.iconDisable = iconDisable;
        this.iconRollOver = iconRollover;
        this.tip = tip;

        initialize();
        setUp();
    }

    /**
     * 初始化，设置按钮属性：无边，无焦点渲染，无内容区，各边距0
     */
    private void initialize() {
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusable(true);
        this.setMargin(new Insets(0, 0, 0, 0));
    }

    /**
     * 设置按钮图标：鼠标移过、按压、失效的图标 和设置按钮提示
     */
    private void setUp() {
		this.setRolloverIcon(iconRollOver);
        this.setSelectedIcon(iconRollOver);
        this.setPressedIcon(iconEnable);
        this.setDisabledIcon(iconDisable);

        if (!tip.equals("")) {
            this.setToolTipText(tip);
        }

    }
}
