package org.qiunet.excel2cfgs.swing.panel;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.enums.OutputFormatType;
import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.listener.JButtonMouseListener;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
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
	private JLabel proCfgLabel;
	private JComboBox<String> proCfgPathChoice;
	private JButton saveButton;
	private JButton addExcelPath;
	private JButton remExcelPath;
	private JPanel operateProCfgPanel;
	private JButton addCfgPath;
	private JButton remCfgPath;

	public SettingPanel() {
		Arrays.stream(OutputFormatType.values()).forEach(this.formatChoice::addItem);

		Arrays.stream(RoleType.values()).forEach(this.roleTypeJComboBox::addItem);
		this.roleTypeJComboBox.addActionListener(e -> this.refreshByRoleType());
		saveButton.addMouseListener(new JButtonMouseListener(saveButton, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
		addCfgPath.addMouseListener(new JButtonMouseListener(addCfgPath, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
		remCfgPath.addMouseListener(new JButtonMouseListener(remCfgPath, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
		addExcelPath.addMouseListener(new JButtonMouseListener(addExcelPath, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
		remExcelPath.addMouseListener(new JButtonMouseListener(remExcelPath, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));

		saveButton.addActionListener(e -> this.saveData());
		addExcelPath.addActionListener(e -> {
			String directoryChooser = this.openDirectoryChooser("选择excel路径文件夹");
			if (directoryChooser == null) {
				return;
			}

			this.excelChoice.addItem(directoryChooser);
			this.excelChoice.setSelectedItem(directoryChooser);
			SettingManager.getInstance().addExcelPath(directoryChooser);
		});
		addCfgPath.addActionListener(e -> {

			String directoryChooser = this.openDirectoryChooser("选择项目配置路径文件夹");
			if (directoryChooser == null) {
				return;
			}

			this.proCfgPathChoice.addItem(directoryChooser);
			this.proCfgPathChoice.setSelectedItem(directoryChooser);
			SettingManager.getInstance().addCfgPath(directoryChooser);
		});
		remCfgPath.addActionListener(e -> {
			String curr = this.proCfgPathChoice.getSelectedItem().toString();
			SettingManager.getInstance().removeCfgPath(curr);
			this.proCfgPathChoice.removeItem(curr);
		});

		remExcelPath.addActionListener(e -> {
			String curr = this.excelChoice.getSelectedItem().toString();
			SettingManager.getInstance().removeExcelPath(curr);
			this.excelChoice.removeItem(curr);
		});
	}

	/**
	 * 选择文件夹路径
	 */
	public String openDirectoryChooser(String title) {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setSize(UiConstant.FILE_CHOOSER_SIZE);
		chooser.setFont(UiConstant.DEFAULT_FONT);
		chooser.setDialogTitle(title);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	@Override
	public void unActivate() {

	}


	/**
	 * 保存数据
	 */
	private void saveData() {
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
		} else {
			OutputFormatType choiceItem = (OutputFormatType) this.formatChoice.getSelectedItem();
			choiceItem.saveStatus();
		}
		SettingManager.getInstance().update();
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
	private void refreshByRoleType() {
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
		} else {
			if (SettingManager.getInstance().getSetting().isXdChecked()) {
				this.formatChoice.setSelectedItem(OutputFormatType.xd);
			} else if (SettingManager.getInstance().getSetting().isXmlChecked()) {
				this.formatChoice.setSelectedItem(OutputFormatType.xml);
			} else if (SettingManager.getInstance().getSetting().isJsonChecked()) {
				this.formatChoice.setSelectedItem(OutputFormatType.json);
			}
		}

		this.operateProCfgPanel.setVisible(selectedItem != RoleType.SCHEMER);
		this.proCfgPathChoice.setVisible(selectedItem != RoleType.SCHEMER);
		this.proCfgLabel.setVisible(selectedItem != RoleType.SCHEMER);
	}

	@Override
	public JPanel getShowPanel() {
		return showPanel;
	}

	@Override
	public IconButtonType type() {
		return IconButtonType.setting;
	}

	@Override
	public void addToParent(ToolTabPanel toolTabPanel) {
		toolTabPanel.getPanelDown().add(this.getButton(), BorderLayout.SOUTH);
	}

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		showPanel = new JPanel();
		showPanel.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
		final JLabel label1 = new JLabel();
		label1.setText("职业选择");
		showPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		showPanel.add(spacer1, new GridConstraints(4, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		roleTypeJComboBox = new JComboBox();
		Font roleTypeJComboBoxFont = this.$$$getFont$$$("Consolas", -1, 18, roleTypeJComboBox.getFont());
		if (roleTypeJComboBoxFont != null) roleTypeJComboBox.setFont(roleTypeJComboBoxFont);
		showPanel.add(roleTypeJComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("输出选择");
		showPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		showPanel.add(panel1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		formatChoice = new JComboBox();
		Font formatChoiceFont = this.$$$getFont$$$(null, -1, 18, formatChoice.getFont());
		if (formatChoiceFont != null) formatChoice.setFont(formatChoiceFont);
		panel1.add(formatChoice, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		formatCheckBoxPanel = new JPanel();
		formatCheckBoxPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(formatCheckBoxPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		jsonBox = new JCheckBox();
		Font jsonBoxFont = this.$$$getFont$$$(null, -1, 18, jsonBox.getFont());
		if (jsonBoxFont != null) jsonBox.setFont(jsonBoxFont);
		jsonBox.setText("json");
		formatCheckBoxPanel.add(jsonBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		xmlBox = new JCheckBox();
		Font xmlBoxFont = this.$$$getFont$$$(null, -1, 18, xmlBox.getFont());
		if (xmlBoxFont != null) xmlBox.setFont(xmlBoxFont);
		xmlBox.setText("xml");
		formatCheckBoxPanel.add(xmlBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		xdBox = new JCheckBox();
		Font xdBoxFont = this.$$$getFont$$$(null, -1, 18, xdBox.getFont());
		if (xdBoxFont != null) xdBox.setFont(xdBoxFont);
		xdBox.setText("xd");
		formatCheckBoxPanel.add(xdBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label3 = new JLabel();
		label3.setText("Excel路径");
		showPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		excelChoice = new JComboBox();
		Font excelChoiceFont = this.$$$getFont$$$("Consolas", -1, 18, excelChoice.getFont());
		if (excelChoiceFont != null) excelChoice.setFont(excelChoiceFont);
		showPanel.add(excelChoice, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(700, -1), new Dimension(700, -1), new Dimension(700, -1), 0, false));
		proCfgLabel = new JLabel();
		proCfgLabel.setText("项目路径");
		showPanel.add(proCfgLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		proCfgPathChoice = new JComboBox();
		Font proCfgPathChoiceFont = this.$$$getFont$$$("Consolas", -1, 18, proCfgPathChoice.getFont());
		if (proCfgPathChoiceFont != null) proCfgPathChoice.setFont(proCfgPathChoiceFont);
		showPanel.add(proCfgPathChoice, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(700, -1), new Dimension(700, -1), new Dimension(700, -1), 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
		showPanel.add(panel2, new GridConstraints(4, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		saveButton = new JButton();
		saveButton.setText("保存设置");
		panel2.add(saveButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		panel2.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final Spacer spacer3 = new Spacer();
		panel2.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		showPanel.add(panel3, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		addExcelPath = new JButton();
		addExcelPath.setText("新增");
		panel3.add(addExcelPath, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		remExcelPath = new JButton();
		remExcelPath.setText("移除");
		panel3.add(remExcelPath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		operateProCfgPanel = new JPanel();
		operateProCfgPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		showPanel.add(operateProCfgPanel, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		addCfgPath = new JButton();
		addCfgPath.setText("新增");
		operateProCfgPanel.add(addCfgPath, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		remCfgPath = new JButton();
		remCfgPath.setText("移除");
		operateProCfgPanel.add(remCfgPath, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
		if (currentFont == null) return null;
		String resultName;
		if (fontName == null) {
			resultName = currentFont.getName();
		} else {
			Font testFont = new Font(fontName, Font.PLAIN, 10);
			if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
				resultName = fontName;
			} else {
				resultName = currentFont.getName();
			}
		}
		Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
		boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
		Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
		return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return showPanel;
	}
}
