package org.qiunet.excel2cfgs.swing.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/***
 *
 *
 * @author qiunet
 * 2021-02-22 09:44
 */
public class JButtonMouseListener extends MouseAdapter {
	private JButton jButton;
	private Color textColor;
	private Color backGroundColor;

	public JButtonMouseListener(JButton jButton, Color textColor, Color backGroundColor) {
		this.jButton = jButton;
		this.textColor = textColor;
		this.backGroundColor = backGroundColor;

		this.mouseExited(null);
		jButton.setSelected(false);
		jButton.setFocusable(false);
		jButton.setFocusPainted(false);
		jButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		jButton.setForeground(backGroundColor);
		jButton.setBackground(textColor);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		jButton.setForeground(textColor);
		jButton.setBackground(backGroundColor);
	}
}
