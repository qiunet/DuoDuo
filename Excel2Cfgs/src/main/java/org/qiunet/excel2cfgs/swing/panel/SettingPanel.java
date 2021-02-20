package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.enums.OutputFormatType;
import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.component.IconButton;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/***
 * 设置面板
 *
 * @author qiunet
 * 2021-02-08 15:35
 */
public class SettingPanel extends IconJPanel {
	private JPanel outputFormatPanel;
	private JLabel projectCfgPathLabel;

	private JComboBox<RoleType> roleTypeJComboBox;
	private JComboBox<String> proCfgPathChoice;
	private JComboBox<String> excelChoice;

	private JComboBox<OutputFormatType> formatChoice;
	private JPanel formatCheckBoxPanel;
	private JCheckBox jsonBox;
	private JCheckBox xmlBox;
	private JCheckBox xdBox;

	private IconButton saveButton;

	private JPopupMenu addOrDeductPopupMenu;
	private JPanel showPanel;

	@Override
    public void initialize() {
//		GridBagLayout gridLayout = new GridBagLayout();
//		this.addOrDeductPopupMenu = new JPopupMenu("下拉右键菜单");
//		this.addOrDeductPopupMenu.add(new JMenuItem("+"));
//		this.addOrDeductPopupMenu.add(new JMenuItem("-"));
//
//		this.formatChoice = new JComboBox<>(OutputFormatType.values());
//		formatCheckBoxPanel = new JPanel(new FlowLayout());
//		this.jsonBox = new JCheckBox("json");
//		this.xmlBox = new JCheckBox("xml");
//		this.xdBox = new JCheckBox("xd");
//		formatCheckBoxPanel.add(this.jsonBox);
//		formatCheckBoxPanel.add(this.xmlBox);
//		formatCheckBoxPanel.add(this.xdBox);
//
//		this.saveButton = new IconButton(UiConstant.SAVE, UiConstant.SAVE_ENABLE, UiConstant.SAVE_DISABLE, UiConstant.SAVE, "保存");
//
//		JLabel roleLabel = new JLabel("角色选择:");
//		JLabel excelPathLabel = new JLabel("Excel路径选择:");
//		JLabel outputFormatLabel = new JLabel("输出格式选择:");
//		this.projectCfgPathLabel = new JLabel("项目输出路径选择:");
//
//		this.roleTypeJComboBox = new JComboBox<>(RoleType.values());
//		this.roleTypeJComboBox.addActionListener(e -> this.refreshByRoleType());
//
//		GridBagConstraints constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
//		gridLayout.setConstraints(roleLabel, constraints);
//
//		constraints = this.createGridBagConstraints(GridBagConstraints.WEST);
//		constraints.gridwidth = GridBagConstraints.REMAINDER;
//		gridLayout.setConstraints(this.roleTypeJComboBox, constraints);
//		this.getShowPanel().add(roleLabel);
//		this.getShowPanel().add(roleTypeJComboBox);
//
//		constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
//		gridLayout.setConstraints(outputFormatLabel, constraints);
//		this.outputFormatPanel = new JPanel(new FlowLayout());
//		constraints = this.createGridBagConstraints(GridBagConstraints.WEST);
//		constraints.gridwidth = GridBagConstraints.REMAINDER;
//		gridLayout.setConstraints(this.outputFormatPanel, constraints);
//		this.getShowPanel().add(outputFormatLabel);
//		this.outputFormatPanel.add(this.formatCheckBoxPanel);
//		this.outputFormatPanel.add(this.formatChoice);
//		this.getShowPanel().add(outputFormatPanel);
//
//		this.excelChoice = new JComboBox<>();
//		this.excelChoice.setMinimumSize(UiConstant.COMBO_BOX_SIZE);
//		constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
//		gridLayout.setConstraints(excelPathLabel, constraints);
//		constraints = this.createGridBagConstraints(GridBagConstraints.WEST);
//		constraints.gridwidth = GridBagConstraints.REMAINDER;
//		gridLayout.setConstraints(this.excelChoice, constraints);
//		this.getShowPanel().add(excelPathLabel);
//		this.getShowPanel().add(excelChoice);
//
//		this.proCfgPathChoice = new JComboBox<>();
//		this.proCfgPathChoice.setMinimumSize(UiConstant.COMBO_BOX_SIZE);
//		constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
//		gridLayout.setConstraints(projectCfgPathLabel, constraints);
//		constraints = this.createGridBagConstraints(GridBagConstraints.WEST);
//		constraints.gridwidth = GridBagConstraints.REMAINDER;
//		gridLayout.setConstraints(this.proCfgPathChoice, constraints);
//		this.getShowPanel().add(projectCfgPathLabel);
//		this.getShowPanel().add(proCfgPathChoice);
//
//		constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
//		constraints.gridwidth = GridBagConstraints.REMAINDER;
//		gridLayout.setConstraints(this.saveButton, constraints);
//		this.getShowPanel().add(saveButton);
//
//		this.saveButton.addActionListener(e -> this.saveData());
//
//		this.getShowPanel().setLayout(gridLayout);
	}

	private GridBagConstraints createGridBagConstraints(int anchor){
    	GridBagConstraints constraints = new GridBagConstraints();
    	constraints.insets = new Insets(10, 20, 10, 20);
    	constraints.anchor = anchor;
    	return constraints;
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
	public void loadData() {
//		this.roleTypeJComboBox.setSelectedItem(SettingManager.getInstance().getSetting().getRoleType());
//
//		excelChoice.removeAllItems();
//		SettingManager.getInstance().getExcelPaths().forEach(excelChoice::addItem);
//		excelChoice.setSelectedItem(SettingManager.getInstance().getFirstExcelPath());
//
//		proCfgPathChoice.removeAllItems();
//		SettingManager.getInstance().getCfgPaths().forEach(proCfgPathChoice::addItem);
//		proCfgPathChoice.setSelectedItem(SettingManager.getInstance().getFirstCfgPath());

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

		this.projectCfgPathLabel.setVisible(selectedItem != RoleType.SCHEMER);
		this.proCfgPathChoice.setVisible(selectedItem != RoleType.SCHEMER);
		this.getShowPanel().updateUI();
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
