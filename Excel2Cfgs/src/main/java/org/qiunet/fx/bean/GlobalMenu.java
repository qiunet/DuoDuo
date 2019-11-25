package org.qiunet.fx.bean;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import org.qiunet.utils.ExcelToStream;
import org.qiunet.utils.FxUIUtil;
import org.qiunet.utils.string.StringUtil;

import java.awt.*;
import java.io.File;

/**
 * created by wgw on 2019/9/7
 */
public class GlobalMenu extends ContextMenu {
	private static GlobalMenu INSTANCE = null;

	private final static String export_id = "export";
	private final static String open_id = "open";


	/**
	 * 私有构造函数
	 */
	private GlobalMenu() {
		MenuItem settingMenuItem = new MenuItem("打开");
		settingMenuItem.setId(open_id);
		MenuItem updateMenuItem = new MenuItem("导出");
		updateMenuItem.setId(export_id);

		getItems().add(settingMenuItem);
		getItems().add(updateMenuItem);

	}

	/**
	 * 获取实例
	 *
	 * @return GlobalMenu
	 */
	public static GlobalMenu getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GlobalMenu();
		}

		return INSTANCE;
	}

	public void addOnAction(final TreeView<FileTreeItem.FileTree> treeView, TextArea msgContent) {
		EventHandler<ActionEvent> handler = new TreeEventHandler(treeView, msgContent);
		getItems().stream().forEach(item -> setOnAction(handler));
	}

	private static class TreeEventHandler implements EventHandler<ActionEvent> {

		private void export(File file) {
			ExcelToStream excelToStream = new ExcelToStream();
			if (file.isDirectory()) {//文件夹操作
				ObservableList<TreeItem<FileTreeItem.FileTree>> list = treeView.getRoot().getChildren();
				try {
					for (TreeItem<FileTreeItem.FileTree> o : list) {
						String msg = excelToStream.excelToStream(o.getValue().getFile());
						FxUIUtil.sendMsgToTextInput(msgContent, msg, true);

					}
				} catch (Exception e) {
					FxUIUtil.sendMsgToTextInput(msgContent, e.getMessage(), true);
				}
			} else if (file.isFile()) {
				FxUIUtil.sendMsgToTextInput(msgContent, excelToStream.excelToStream(file), true);
			}

		}

		final TreeView<FileTreeItem.FileTree> treeView;
		final TextArea msgContent;

		public TreeEventHandler(TreeView<FileTreeItem.FileTree> treeView, TextArea msgContent) {
			this.treeView = treeView;
			this.msgContent = msgContent;
		}

		@Override
		public void handle(ActionEvent event) {
			if (treeView == null) return;
			Object tag = event.getTarget();
			if (tag instanceof MenuItem) {
				TreeItem<FileTreeItem.FileTree> treeItem = treeView.getSelectionModel().getSelectedItem();
				if (treeItem == null) return;
				File file = treeItem.getValue().getFile();
				MenuItem menuItem = (MenuItem) tag;
				try {
					switch (menuItem.getId()) {
						case open_id:
							Desktop.getDesktop().open(file);
							break;
						case export_id:
							export(file);
							break;
						default:
							throw new RuntimeException("无法识别id:" + menuItem.getId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	}

}
