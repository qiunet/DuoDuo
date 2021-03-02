package org.qiunet.excel2cfgs.swing;

import org.qiunet.excel2cfgs.AppMain;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.panel.CfgPanel;
import org.qiunet.utils.async.LazyLoader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/***
 *
 * @Author qiunet
 * @Date 2021/2/21 15:24
 **/
public class SwingUtil {
	private static final LazyLoader<JTextArea> console = new LazyLoader<>(() -> ((CfgPanel) (IconButtonType.cfg.getPanel())).getConsole());
	/**
	 * 弹出错误信息
	 * @param message
	 */
	public static void alterError(String message) {
		alterMessage(message, "错误信息", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 弹出消息
	 * @param message
	 * @param title
	 * @param messageType
	 */
	public static void alterMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(AppMain.instance.getStatusPanel(), message, title, messageType);
	}

	/**
	 * 提示信息
	 * @param message
	 */
	public static void promptMessage(String message) {
		alterMessage(message, "提示", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 拼加一条消息到输出控制台
	 * @param msg
	 */
	public static void appendToConsole(String msg) {
		sendMsgToConsole(msg, true);
	}
	/**
	 * 发送信息到文本控件
	 * @param msg
	 * @param append 是否追加
	 */
	public static void sendMsgToConsole(String msg, boolean append) {
		if (console.get() == null) {
			alterError(msg);
			return;
		}
		if (append) {
			console.get().append(msg);
			console.get().append("\n");
		} else {
			console.get().setText(msg);
		}

	}

	public static void open(File file) {
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			alterError("打开文件目录异常: "+e.getMessage());
		}
	}
}
