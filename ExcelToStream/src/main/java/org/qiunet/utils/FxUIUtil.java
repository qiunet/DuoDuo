package org.qiunet.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;

/**
 * created by wgw on 2019/7/29
 */
public class FxUIUtil {
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
			alert.setTitle(title);
			alert.showAndWait();
		});

	}

	/**
	 * 发送信息到文本控件
	 * @param input
	 * @param msg
	 * @param append 是否追加
	 */
	public static void sendMsgToTextInput(TextInputControl input, String msg, boolean append) {
		if (input == null) {
			openAlert(Alert.AlertType.ERROR, msg, "error");
			return;
		}
		addUITask(() -> {
			if (append) {
				input.appendText(msg);
				input.appendText("\n");
			}
			else input.setText(msg);

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
