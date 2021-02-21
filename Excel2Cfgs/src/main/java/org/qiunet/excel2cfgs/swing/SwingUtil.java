package org.qiunet.excel2cfgs.swing;

import org.qiunet.excel2cfgs.AppMain;

import javax.swing.*;

/***
 *
 * @Author qiunet
 * @Date 2021/2/21 15:24
 **/
public class SwingUtil {
	/**
	 * 弹出错误信息
	 * @param message
	 */
	public static void alterError(String message) {
		JOptionPane.showMessageDialog(AppMain.instance.getStatusPanel(), message);
	}
}
