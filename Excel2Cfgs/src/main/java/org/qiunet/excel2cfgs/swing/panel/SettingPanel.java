package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.enums.OutputFormatType;
import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.listener.JButtonMouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;

/***
 * 设置面板
 *
 * @author qiunet
 * 2021-02-08 15:35
 */
public class SettingPanel extends IconJPanel {
	private JPanel showPanel;
	private JComboBox<OutputFormatType> formatChoice;
	private JComboBox<RoleType> roleTypeJComboBox;
	private JCheckBox jsonBox;
	private JCheckBox xmlBox;
	private JCheckBox xdBox;
	private JPanel formatCheckBoxPanel;
	private JComboBox<String> excelChoice;
	private JPanel proCfgPathChoicePanel;
	private JComboBox<String> proCfgPathChoice;
	private JButton saveButton;

	public SettingPanel() {
		Arrays.stream(OutputFormatType.values()).forEach(this.formatChoice::addItem);

		Arrays.stream(RoleType.values()).forEach(this.roleTypeJComboBox::addItem);
		this.roleTypeJComboBox.addActionListener(e -> this.refreshByRoleType());
		saveButton.addMouseListener(new JButtonMouseListener(saveButton, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
	}

	@Override
	public void unActivate() {

	}


	/**
	 * 保存数据
	 */
	private void saveData(){
		RoleType selectedItem = (RoleType) this.roleTypeJComboBox.getSelectedItem();
		SettingManager.getInstance().getSetting().setRoleType(selectedItem);
		SettingManager.getInstance().addExcelPath(Objects.requireNonNull(this.excelChoice.getSelectedItem()).toString());
		SettingManager.getInstance().addCfgPath(Objects.requireNonNull(this.proCfgPathChoice.getSelectedItem()).toString());


		SettingManager.getInstance().getSetting().setJsonChecked(false);
		SettingManager.getInstance().getSetting().setXmlChecked(false);
		SettingManager.getInstance().getSetting().setXdChecked(false);
		if (selectedItem == RoleType.SCHEMER) {
			SettingManager.getInstance().getSetting().setJsonChecked(this.jsonBox.isSelected());
			SettingManager.getInstance().getSetting().setXmlChecked(this.xmlBox.isSelected());
			SettingManager.getInstance().getSetting().setXdChecked(this.xdBox.isSelected());
		}else {
			OutputFormatType choiceItem = (OutputFormatType) this.formatChoice.getSelectedItem();
			choiceItem.saveStatus();
		}
	}

	@Override
	public void activate() {
		this.roleTypeJComboBox.setSelectedItem(SettingManager.getInstance().getSetting().getRoleType());

		excelChoice.removeAllItems();
		SettingManager.getInstance().getExcelPaths().forEach(excelChoice::addItem);
		excelChoice.setSelectedItem(SettingManager.getInstance().getFirstExcelPath());

		proCfgPathChoice.removeAllItems();
		SettingManager.getInstance().getCfgPaths().forEach(proCfgPathChoice::addItem);
		proCfgPathChoice.setSelectedItem(SettingManager.getInstance().getFirstCfgPath());

	}

	/**
	 * 根据roleType 变动刷新
	 */
	private void refreshByRoleType(){
		RoleType selectedItem = (RoleType) roleTypeJComboBox.getSelectedItem();
		this.formatCheckBoxPanel.setVisible(false);
		this.formatChoice.setVisible(false);
		if (selectedItem != null) {
			this.formatCheckBoxPanel.setVisible(selectedItem == RoleType.SCHEMER);
			this.formatChoice.setVisible(selectedItem != RoleType.SCHEMER);
		}

		if (selectedItem == RoleType.SCHEMER) {
			this.jsonBox.setSelected(SettingManager.getInstance().getSetting().isJsonChecked());
			this.xmlBox.setSelected(SettingManager.getInstance().getSetting().isXmlChecked());
			this.xdBox.setSelected(SettingManager.getInstance().getSetting().isXdChecked());
		}else {
			if (SettingManager.getInstance().getSetting().isXdChecked()) {
				this.formatChoice.setSelectedItem(OutputFormatType.xd);
			} else if (SettingManager.getInstance().getSetting().isXmlChecked()) {
				this.formatChoice.setSelectedItem(OutputFormatType.xml);
			}else if (SettingManager.getInstance().getSetting().isJsonChecked()) {
				this.formatChoice.setSelectedItem(OutputFormatType.json);
			}
		}

		this.proCfgPathChoicePanel.setVisible(selectedItem != RoleType.SCHEMER);
	}

	@Override
	public JPanel getShowPanel() {
		return showPanel;
	}

	@Override
	public IconButtonType type(){
		return IconButtonType.setting;
	}

	@Override
	public void addToParent(ToolTabPanel toolTabPanel) {
		toolTabPanel.getPanelDown().add(this.getButton(), BorderLayout.SOUTH);
	}
}
