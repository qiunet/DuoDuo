package org.qiunet.frame.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTreeCell;
import org.qiunet.utils.Excel2CfgsUtil;
import org.qiunet.utils.ExcelToCfg;

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

	private TextArea console;

	public CfgTreeCell(TextArea console){
		super(new FileStringConvert());

		this.console = console;
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

				new ExcelToCfg(fileOrDir, console).excelToStream();
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
		if (item.isDirectory()) {
			EventHandler<ActionEvent> handler = event -> {
				try {
					Desktop.getDesktop().open(item);
				} catch (IOException e) {
					e.printStackTrace();
				}
			};

			MenuItem open = new MenuItem("打开文件夹");
			open.setOnAction(handler);

			MenuItem menuItem2 = new MenuItem("转换该文件夹所有配置");
			menuItem2.setOnAction(event -> this.exportCfgs(item));

			menu = new ContextMenu(open, menuItem2);
		}else {
			MenuItem convert = new MenuItem("转换");
			convert.setOnAction(event -> this.exportCfgs(item));
			menu = new ContextMenu(convert);
		}
		this.setContextMenu(menu);
	}
}
