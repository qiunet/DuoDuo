package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.swing.IToolPanel;

import javax.swing.*;

/***
 * 配置转换的面板
 *
 * @Author qiunet
 * @Date 2021/2/9 21:56
 **/
public class CfgPanel extends JPanel implements IToolPanel {
	@Override
	public void reload() {
		this.add(new JLabel("测试"));
	}
}
