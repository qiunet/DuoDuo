package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.IToolPanel;

import javax.swing.*;
import java.awt.*;

/***
 * 设置面板
 *
 * @author qiunet
 * 2021-02-08 15:35
 */
public class SettingPanel extends JPanel implements IToolPanel {
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
        super(true);
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
		this.setVisible(true);
    }

	@Override
	public void reload() {
		this.roleTypeJComboBox.setSelectedItem(SettingManager.getInstance().getSetting().getRoleType());
	}
}
