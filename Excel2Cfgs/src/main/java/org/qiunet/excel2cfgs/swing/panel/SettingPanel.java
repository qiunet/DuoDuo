package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.enums.RoleType;

import javax.swing.*;
import java.awt.*;

/***
 * 设置面板
 *
 * @author qiunet
 * 2021-02-08 15:35
 */
public class SettingPanel extends JPanel {
    /**
     * 角色 label
     */
    private JLabel roleLabel;
    /**
     * excel 路径label
     */
    private JLabel excelPathLabel;
    private JLabel outputFormatLabel;
    private JLabel projectCfgPathLabel;
    private JComboBox<RoleType> roleTypeJComboBox;

    public SettingPanel() {
        super(true);
        this.initialize();
    }

    private void initialize() {
        this.setLayout(new GridLayout(4, 2));

        roleTypeJComboBox = new JComboBox<>();
        for (RoleType value : RoleType.values()) {
            roleTypeJComboBox.addItem(value);
        }
    }
}
