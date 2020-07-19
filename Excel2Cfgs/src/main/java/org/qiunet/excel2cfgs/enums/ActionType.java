package org.qiunet.excel2cfgs.enums;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.qiunet.excel2cfgs.frame.RootController;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.utils.FxUIUtil;
import org.qiunet.excel2cfgs.utils.SvnUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/***
 *
 *
 * qiunet
 * 2019-11-06 11:34
 ***/
public enum ActionType {
	/***
	 * 选择 excel的路径
	 */
	excelPathChoice {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			String path = openDirectoryChooser(primaryStage, "选择你的excel路径");
			if (path == null) return;

			if (SettingManager.getInstance().addExcelPath(path)){
				controller.excelPaths.getItems().add(path);
			}
			controller.excelPaths.getSelectionModel().select(path);
		}
	},
	/***
	 * 删除excel的路径
	 */
	excelPathRem {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			boolean empty = controller.excelPaths.getSelectionModel().isEmpty();
			if (empty) return;

			String item = controller.excelPaths.getSelectionModel().getSelectedItem();
			SettingManager.getInstance().removeExcelPath(item);
			controller.excelPaths.getItems().remove(item);

			String nextDefaultPath = SettingManager.getInstance().getFirstExcelPath();
			if (nextDefaultPath != null) {
				controller.excelPaths.getSelectionModel().select(nextDefaultPath);
			}
		}
	},
	/***
	 * 刷新当前的excel文件
	 */
	excelRefresh {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			controller.refreshExcelTree();
		}
	},
	/***
	 * 更新excel表
	 */
	svnUpdate {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			String path = controller.excelPaths.getSelectionModel().getSelectedItem();
			SvnUtil.svnEvent(SvnUtil.SvnCommand.UPDATE, path);
		}
	},
	/***
	 * 清理 svn状态
	 */
	svnClean {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			String path = controller.excelPaths.getSelectionModel().getSelectedItem();
			SvnUtil.svnEvent(SvnUtil.SvnCommand.CLEANUP, path);
		}
	},
	/**
	 * 提交excel表
	 */
	svnCommit {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			String path = controller.excelPaths.getSelectionModel().getSelectedItem();
			SvnUtil.svnEvent(SvnUtil.SvnCommand.COMMIT, path);
		}
	},

	/**
	 * 配置文件路径的选择
	 */
	cfgPathChoice {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			String path = openDirectoryChooser(primaryStage, "选择你的项目配置文件夹");
			if (path == null) {
				return;
			}

			if (SettingManager.getInstance().addCfgPath(path)){
				controller.cfgPaths.getItems().add(path);
			}
			controller.cfgPaths.getSelectionModel().select(path);
		}
	},
	/***
	 *
	 */
	cfgPathRem {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			boolean empty = controller.cfgPaths.getSelectionModel().isEmpty();
			if (empty) return;

			String item = controller.cfgPaths.getSelectionModel().getSelectedItem();
			SettingManager.getInstance().removeCfgPath(item);
			controller.cfgPaths.getItems().remove(item);

			String nextDefaultPath = SettingManager.getInstance().getFirstCfgPath();
			if (nextDefaultPath != null) {
				controller.cfgPaths.getSelectionModel().select(nextDefaultPath);
			}
			SettingManager.getInstance().update();
		}
	},

	/***
	 * 勾选xd
	 */
	xdBox {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event, RootController controller) {
			boolean selected = ((CheckBox) event.getSource()).isSelected();
			SettingManager.getInstance().getSetting().setXdChecked(selected);
			SettingManager.getInstance().update();
		}
	},

	/***
	 * 勾选json
	 */
	jsonBox {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event, RootController controller) {
			boolean selected = ((CheckBox) event.getSource()).isSelected();
			SettingManager.getInstance().getSetting().setJsonChecked(selected);
			SettingManager.getInstance().update();
		}
	},
	/***
	 * 勾选 xml
	 */
	xmlBox {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event, RootController controller) {
			boolean selected = ((CheckBox) event.getSource()).isSelected();
			SettingManager.getInstance().getSetting().setXmlChecked(selected);
			SettingManager.getInstance().update();
		}
	},
	/***
	 * 打开cfg的路径
	 */
	openCfgPath {
		@Override
		public void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller) {
			boolean empty = controller.cfgPaths.getSelectionModel().isEmpty();
			if (empty) {
				FxUIUtil.openAlert(Alert.AlertType.INFORMATION, "没有选中的文件夹", "提示");
				return;
			}

			String selectedItem = controller.cfgPaths.getSelectionModel().getSelectedItem();
			try {
				Desktop.getDesktop().open(new File(selectedItem));
			} catch (IOException e) {
				FxUIUtil.alterError("打开错误:"+e.getMessage());
				e.printStackTrace();
			}
		}
	},
	;

	/***
	 * 处理点击事件
	 */
	public abstract void handlerAction(Stage primaryStage, ActionEvent event,  RootController controller);

	/**
	 * 选择文件夹路径
	 */
	public String openDirectoryChooser(Stage stage, String title) {
		DirectoryChooser chooser = new DirectoryChooser();
		File defFile = new File(System.getProperty("user.home"));
		chooser.setInitialDirectory(defFile);
		chooser.setTitle(title);
		File checkFile = chooser.showDialog(stage);
		if (checkFile == null) return null;

		return checkFile.getAbsolutePath();
	}
}
