package org.qiunet.excel2cfgs.swing.panel;

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
    /**
     * 角色 label
     */
    private JLabel roleLabel;
    /**
     * excel 路径label
     */
    private JLabel excelPathLabel;
    private JLabel outputFormatLabel;
	private JPanel outputFormatPanel;
	private JLabel projectCfgPathLabel;

	private JComboBox<RoleType> roleTypeJComboBox;
    public SettingPanel() {
        this.initialize();
    }

    public void initialize() {
        this.setLayout(new GridLayout(4, 2));
		this.roleLabel = new JLabel("角色选择:");
		this.excelPathLabel = new JLabel("Excel路径选择:");
		this.outputFormatLabel = new JLabel("输出格式选择:");
		this.projectCfgPathLabel = new JLabel("项目输出路径选择:");

		this.roleTypeJComboBox = new JComboBox<>();
		for (RoleType value : RoleType.values()) {
			roleTypeJComboBox.addItem(value);
		}
		this.outputFormatPanel = new JPanel(true);
        this.add(this.roleLabel);
		this.add(this.roleTypeJComboBox);
		this.add(this.outputFormatLabel);
		this.add(this.outputFormatPanel);
		this.add(this.excelPathLabel);
		this.add(this.projectCfgPathLabel);
    }

	@Override
	public void loadData() {
		this.roleTypeJComboBox.setSelectedItem(SettingManager.getInstance().getSetting().getRoleType());
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
