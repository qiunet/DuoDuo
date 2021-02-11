package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;

import javax.swing.*;
import java.awt.*;

/***
 * 设置面板
 *
 * @author qiunet
 * 2021-02-08 15:35
 */
public class SettingPanel extends IconJPanel {
	private JPanel outputFormatPanel;
	private JComboBox<RoleType> roleTypeJComboBox;
	private JComboBox<String> proCfgPathChoice;
	private JComboBox<String> excelChoice;

    public SettingPanel() {
        this.initialize();
    }

    @Override
    public void initialize() {
		GridBagLayout gridLayout = new GridBagLayout();

		JLabel roleLabel = new JLabel("角色选择:");
		JLabel excelPathLabel = new JLabel("Excel路径选择:");
		JLabel outputFormatLabel = new JLabel("输出格式选择:");
		JLabel projectCfgPathLabel = new JLabel("项目输出路径选择:");

		this.roleTypeJComboBox = new JComboBox<>();
		for (RoleType value : RoleType.values()) {
			roleTypeJComboBox.addItem(value);
		}
		GridBagConstraints constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
		gridLayout.setConstraints(roleLabel, constraints);

		constraints = this.createGridBagConstraints(GridBagConstraints.WEST);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridLayout.setConstraints(this.roleTypeJComboBox, constraints);
		this.add(roleLabel);
		this.add(roleTypeJComboBox);

		constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
		gridLayout.setConstraints(outputFormatLabel, constraints);
		this.outputFormatPanel = new JPanel(true);
		constraints = this.createGridBagConstraints(GridBagConstraints.WEST);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridLayout.setConstraints(this.outputFormatPanel, constraints);
		this.add(outputFormatLabel);
		this.add(outputFormatPanel);

		this.excelChoice = new JComboBox<>();
		this.excelChoice.setMinimumSize(UiConstant.COMBO_BOX_SIZE);
		constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
		gridLayout.setConstraints(excelPathLabel, constraints);
		constraints = this.createGridBagConstraints(GridBagConstraints.WEST);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridLayout.setConstraints(this.excelChoice, constraints);
		this.add(excelPathLabel);
		this.add(excelChoice);

		this.proCfgPathChoice = new JComboBox<>();
		this.proCfgPathChoice.setMinimumSize(UiConstant.COMBO_BOX_SIZE);
		constraints = this.createGridBagConstraints(GridBagConstraints.EAST);
		gridLayout.setConstraints(projectCfgPathLabel, constraints);
		constraints = this.createGridBagConstraints(GridBagConstraints.WEST);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridLayout.setConstraints(this.proCfgPathChoice, constraints);
		this.add(projectCfgPathLabel);
		this.add(proCfgPathChoice);

		this.setLayout(gridLayout);
	}

	private GridBagConstraints createGridBagConstraints(int anchor){
    	GridBagConstraints constraints = new GridBagConstraints();
    	constraints.insets = new Insets(10, 20, 10, 20);
    	constraints.anchor = anchor;
    	return constraints;
	}

	@Override
	public void loadData() {
		this.roleTypeJComboBox.setSelectedItem(SettingManager.getInstance().getSetting().getRoleType());

		excelChoice.removeAllItems();
		SettingManager.getInstance().getExcelPaths().forEach(excelChoice::addItem);
		excelChoice.setSelectedItem(SettingManager.getInstance().getFirstExcelPath());

		proCfgPathChoice.removeAllItems();
		SettingManager.getInstance().getCfgPaths().forEach(proCfgPathChoice::addItem);
		proCfgPathChoice.setSelectedItem(SettingManager.getInstance().getFirstCfgPath());
	}


	@Override
	public IconButtonType type(){
		return IconButtonType.setting;
	}

	@Override
	public String title() {
		return "设置";
	}


	@Override
	public void addToParent(ToolTabPanel toolTabPanel) {
		toolTabPanel.getPanelDown().add(this.getButton(), BorderLayout.SOUTH);
	}
}
