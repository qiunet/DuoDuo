package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.AppMain;
import org.qiunet.excel2cfgs.swing.IconButtonManager;
import org.qiunet.excel2cfgs.swing.component.IconButton;
import org.qiunet.excel2cfgs.swing.enums.ButtonStatus;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;

/***
 *
 * @Author qiunet
 * @Date 2021/2/9 21:50
 **/
public interface IIconPanel {
	/**
	 * 初始化.
	 * 把布局做好.
	 */
	void initialize();
	/**
	 * 数据加载.
	 */
	void loadData();

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
	String title();
	/**
	 * 得到button
	 * @return
	 */
	IconButton getButton();

	default void addListener() {
		this.getButton().addActionListener(e -> {
			for (IIconPanel value : IconButtonManager.instance.getIconPanels()) {
				value.getButton().setEnabled(value != this);
				if (value == this) {
					value.getButton().setIcon(getImageIcon(value, ButtonStatus.enable));
				}else {
					value.getButton().setIcon(getImageIcon(value, ButtonStatus.normal));
				}
			}
			AppMain.instance.getMainPanelCenter().removeAll();
			this.loadData();
			AppMain.instance.getTitleLabel().setText(this.title());
			AppMain.instance.getTitleLabel().updateUI();
			AppMain.instance.getMainPanelCenter().add((JPanel) this);
			AppMain.instance.getMainPanelCenter().updateUI();
		});
	}

	/**
	 * 获得图标
	 * @param status
	 * @return
	 */
	static ImageIcon getImageIcon(IIconPanel buttons, ButtonStatus status) {
		return new ImageIcon(AppMain.class.getResource("/icon/"+buttons.name() + "_" + status.name() +".png"));
	}
}
