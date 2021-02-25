package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.AppMain;
import org.qiunet.excel2cfgs.swing.component.IconButton;
import org.qiunet.excel2cfgs.swing.enums.ButtonStatus;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;

/***
 * 左边tab面板接口.
 * 负责一个button和一个面板提供.
 * 面板提供给主界面展示.
 *
 * @Author qiunet
 * @Date 2021/2/9 21:50
 **/
public interface IIconPanel {
	/**
	 * 激活面板时候.
	 * 数据加载.
	 */
	void activate();
	/**
	 * 非激活状态.
	 * 处理部分线程池回收等问题
	 */
	void unActivate();

	/**
	 * 添加到toolPanel
	 * @param toolTabPanel
	 */
	void addToParent(ToolTabPanel toolTabPanel);

	/**
	 * @return
	 */
	IconButtonType type();

	/**
	 * 名称 icon图片前缀
	 *
	 * @return
	 */
	default String name() {
		return type().name();
	}
	/**
	 * tip
	 * @return
	 */
	default String title() {
		return type().getTitle();
	}
	/**
	 * 得到button
	 * @return
	 */
	IconButton getButton();

	default void addListener() {
		this.getButton().addActionListener(e -> {
			for (IconButtonType type : IconButtonType.values()) {
				IIconPanel value = type.getPanel();
				if (! value.getButton().isEnabled()) {
					// 说明当前是激活状态.
					this.unActivate();
				}

				value.getButton().setEnabled(value != this);
				if (value == this) {
					value.getButton().setIcon(getImageIcon(value, ButtonStatus.enable));
				}else {
					value.getButton().setIcon(getImageIcon(value, ButtonStatus.normal));
				}
			}
			AppMain.instance.getMainPanelCenter().removeAll();
			this.activate();
			AppMain.instance.getBorder().setTitle(this.title());
			AppMain.instance.getMainPanelCenter().add(getShowPanel());
			AppMain.instance.getMainPanelCenter().updateUI();
		});
	}

	/**
	 * 获得
	 * @return
	 */
	JPanel getShowPanel();
	/**
	 * 获得图标
	 * @param status
	 * @return
	 */
	static ImageIcon getImageIcon(IIconPanel buttons, ButtonStatus status) {
		return new ImageIcon(AppMain.class.getResource("/icon/"+buttons.name() + "_" + status.name() +".png"));
	}
}
