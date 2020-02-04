package org.qiunet.excel2cfgs.frame.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import org.qiunet.excel2cfgs.utils.Excel2CfgsUtil;
import org.qiunet.excel2cfgs.utils.ExcelToCfg;
import org.qiunet.excel2cfgs.utils.SvnUtil;
import org.qiunet.utils.system.OSUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/***
 *
 *
 * @author qiunet
 * 2020-01-19 16:51
 ***/
public class CfgTreeCell extends TextFieldTreeCell<File> {
	public CfgTreeCell(){
		super(new FileStringConvert());
	}
	/***
	 * 递归 循环所有的配置.
	 * @param fileOrDir
	 */
	private void exportCfgs(File fileOrDir) {
		if (fileOrDir.isDirectory()) {
			for (File file : Objects.requireNonNull(fileOrDir.listFiles())) {
				this.exportCfgs(file);
			}
		}else {
			if (Excel2CfgsUtil.filePostfixCheck(fileOrDir)) {

				new ExcelToCfg(fileOrDir).excelToStream();
			}
		}
	}
	@Override
	public void updateItem(File item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			return;
		}

		ContextMenu menu;
		EventHandler<ActionEvent> handler = event -> {
			try {
				if (item.isDirectory()) {
					Desktop.getDesktop().open(item);
				}else {
					Desktop.getDesktop().open(item.getParentFile());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		MenuItem openDir = new MenuItem("打开文件夹");
		openDir.setOnAction(handler);

		if (item.isDirectory()) {
			MenuItem menuItem2 = new MenuItem("转换该文件夹所有配置");
			menuItem2.setOnAction(event -> this.exportCfgs(item));

			menu = new ContextMenu(menuItem2, openDir);
		}else {
			MenuItem convert = new MenuItem("转换");
			convert.setOnAction(event -> this.exportCfgs(item));
			menu = new ContextMenu(openDir, convert);
		}

		MenuItem svnUpdate = new MenuItem("更新");
		svnUpdate.setOnAction(event -> SvnUtil.svnEvent(SvnUtil.SvnCommand.UPDATE, item.getAbsolutePath()));
		MenuItem svnCommit = new MenuItem("提交");
		svnCommit.setOnAction(event -> SvnUtil.svnEvent(SvnUtil.SvnCommand.COMMIT, item.getAbsolutePath()));
		if (OSUtil.isMac() || OSUtil.isLinux()) {
			svnCommit.setVisible(false);
		}
		menu.getItems().addAll(svnUpdate, svnCommit);
		this.setContextMenu(menu);
	}
}
