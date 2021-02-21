package org.qiunet.excel2cfgs.swing.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/***
 *
 * @Author qiunet
 * @Date 2021/2/21 15:04
 **/
public class JTextFieldHintListener implements FocusListener {
	private final JTextField textField;
	private final String defaultText;

	public JTextFieldHintListener(JTextField textField, String defaultText) {
		this.textField = textField;
		this.defaultText = defaultText;

		//默认直接显示
		textField.setText(defaultText);
		textField.setForeground(Color.GRAY);
	}


	@Override
	public void focusGained(FocusEvent e) {
		//获取焦点时，清空提示内容
		String temp = textField.getText();
		if(temp.equals(defaultText)) {
			textField.setText("");
			textField.setForeground(Color.BLACK);
		}

	}

	@Override
	public void focusLost(FocusEvent e) {
		//失去焦点时，没有输入内容，显示提示内容
		String temp = textField.getText();
		if(temp.equals("")) {
			textField.setForeground(Color.GRAY);
			textField.setText(defaultText);
		}
	}

	public String getDefaultText() {
		return defaultText;
	}
}
