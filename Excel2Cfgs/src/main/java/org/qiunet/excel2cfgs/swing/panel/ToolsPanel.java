package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.swing.SwingUtil;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.listener.JButtonMouseListener;
import org.qiunet.excel2cfgs.swing.listener.JTextFieldHintListener;
import org.qiunet.utils.string.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/***
 * 工具集合
 *
 * @author qiunet
 * 2021-03-02 12:12
 */
public class ToolsPanel extends IconJPanel implements ClipboardOwner {
	private JPanel showPanel;
    private JTextField hashCodeInput;
	private JButton hashcodeConvert;
	private JLabel hashCodeOutput;
	private final Clipboard clipboard;
	private static final String hashcodeInputDefaultText = "输入获取hashcode";

	public ToolsPanel() {
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		hashcodeConvert.addMouseListener(new JButtonMouseListener(hashcodeConvert, UiConstant.TOOL_BAR_BACK_COLOR, Color.WHITE));
		hashCodeInput.addFocusListener(new JTextFieldHintListener(hashCodeInput, hashcodeInputDefaultText));
		hashcodeConvert.addActionListener(e -> {
			String val = hashCodeInput.getText();
			if (StringUtil.isEmpty(val) || val.equals(hashcodeInputDefaultText)) {
				hashCodeOutput.setText("");
				return;
			}
			hashCodeOutput.setText(String.valueOf(val.hashCode()));
		});
		hashCodeOutput.setForeground(UiConstant.TOOL_BAR_BACK_COLOR);
		hashCodeOutput.setFont(UiConstant.DEFAULT_FONT);
		hashCodeOutput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					clipboard.setContents(new StringSelection(hashCodeOutput.getText()), ToolsPanel.this);
					SwingUtil.promptMessage("复制成功");
				}
			}
		});
	}

	@Override
	public void activate() {
		hashCodeInput.setText(hashcodeInputDefaultText);
		hashCodeOutput.setText("");
	}

	@Override
	public void unActivate() {

	}

	@Override
	public void addToParent(ToolTabPanel toolTabPanel) {
		toolTabPanel.getPanelUp().add(getButton());
	}

	@Override
	public IconButtonType type() {
		return IconButtonType.tools;
	}

	@Override
	public JPanel getShowPanel() {
		return showPanel;
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {

	}
}
