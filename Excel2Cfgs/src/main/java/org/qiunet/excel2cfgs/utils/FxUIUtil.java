package org.qiunet.excel2cfgs.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;
import org.qiunet.excel2cfgs.frame.RootController;

/**
 * created by wgw on 2019/7/29
 */
public class FxUIUtil {
	private static final TextInputControl control = RootController.getInstance().console;

	/***
	 * 弹出错误信息
	 * @param errorMsg
	 */
	public static void alterError(String errorMsg) {
		openAlert(Alert.AlertType.ERROR, errorMsg, "错误");
	}

	/**
	 * 打开一个弹窗
	 *
	 * @param alertType
	 * @param msg
	 * @param title
	 */
	public static void openAlert(Alert.AlertType alertType, String msg, String title) {
		addUITask(() -> {
			Alert alert = new Alert(alertType);
			alert.setContentText(msg);
			alert.setHeaderText(null);
			alert.setTitle(title);
			alert.showAndWait();
		});

	}

	/**
	 * 拼加一条消息到输出控制台
	 * @param msg
	 */
	public static void appendMessage(String msg) {
		sendMsgToTextInput(msg, true);
	}
	/**
	 * 发送信息到文本控件
	 * @param msg
	 * @param append 是否追加
	 */
	public static void sendMsgToTextInput(String msg, boolean append) {
		if (control == null) {
			openAlert(Alert.AlertType.ERROR, msg, "error");
			return;
		}
		addUITask(() -> {
			if (append) {
				control.appendText(msg);
				control.appendText("\n");
			}
			else control.setText(msg);

		});
	}

	/**
	 * 将更新ui的任务提交到ui线程
	 * @param task
	 */
	public static void addUITask(Runnable task) {
		Platform.runLater(task);
	}

}
