package org.qiunet.fx.bean;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.qiunet.frame.base.JframeManager;
import org.qiunet.frame.view.MainFrame;
import org.qiunet.utils.ExcelToStream;

import javax.swing.*;
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

	public void addOnAction(final TreeView<File> treeView){
		EventHandler<ActionEvent> handler=new TreeEventHandler(treeView);
		getItems().stream().forEach(item->setOnAction(handler));
	}

	private static class TreeEventHandler implements EventHandler<ActionEvent> {


		final  TreeView<File> treeView;

		public TreeEventHandler (TreeView<File> treeView){
			this.treeView=treeView;
		}

		@Override
		public void handle(ActionEvent event) {
			if(treeView==null) return;
			Object tag = event.getTarget();
			if (tag instanceof MenuItem) {
				TreeItem<File> treeItem=treeView.getSelectionModel().getSelectedItem();
				if(treeItem==null) return;
				File file=treeItem.getValue();
				MenuItem menuItem = (MenuItem) tag;
				try {
					switch (menuItem.getId()) {
						case open_id:
							Desktop.getDesktop().open(file);
							break;
						case export_id:
							System.out.println("导出："+file.getName());
							if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")) {
								ExcelToStream excelToStream = new ExcelToStream();
								String msg = excelToStream.excelToStream(file);
								if (msg != null){
									System.out.println("错误:["+msg+"]");
//									JOptionPane.showMessageDialog(scrollPane, "错误:["+msg+"]");
								}
								System.out.println("SUCCESS");
//								JOptionPane.showMessageDialog(scrollPane, "SUCCESS");
//								JframeManager.getInstance().getJframe(MainFrame.class).loadFileTree();
							}
							break;
						default:
							throw new RuntimeException("无法识别id:"+menuItem.getId());
					}
				}
				catch (Exception e){
					e.printStackTrace();
				}

			}

		}
	}

}
