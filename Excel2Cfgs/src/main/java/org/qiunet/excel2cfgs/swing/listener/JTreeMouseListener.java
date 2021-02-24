package org.qiunet.excel2cfgs.swing.listener;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.swing.SwingUtil;
import org.qiunet.excel2cfgs.swing.panel.CfgPanel;
import org.qiunet.excel2cfgs.utils.Excel2CfgsUtil;
import org.qiunet.excel2cfgs.utils.ExcelToCfg;
import org.qiunet.excel2cfgs.utils.SvnUtil;
import org.qiunet.utils.system.OSUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/***
 *
 *
 * @author qiunet
 * 2021-02-23 17:42
 */
public class JTreeMouseListener extends MouseAdapter {
	private final JTree jTree;
	private final JMenuItem openItem;
	private final JMenuItem convertItem;
	private final JMenuItem svnUpdateItem;
	private final JMenuItem svnCommitItem;

	public JTreeMouseListener(JTree jTree) {
		this.jTree = jTree;
		this.openItem = this.createMenuItem("打开", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (jTree.getSelectionPath() == null) {
					return;
				}

				CfgPanel.FileNode fileNode = (CfgPanel.FileNode) ((DefaultMutableTreeNode) jTree.getSelectionPath().getLastPathComponent()).getUserObject();
				if (fileNode == null) {
					return;
				}

				SwingUtil.open(fileNode.getFile());
			}
		});

		this.convertItem = this.createMenuItem("==", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (jTree.getSelectionPath() == null) {
					return;
				}

				CfgPanel.FileNode fileNode = (CfgPanel.FileNode) ((DefaultMutableTreeNode) jTree.getSelectionPath().getLastPathComponent()).getUserObject();
				if (fileNode == null) {
					return;
				}
				exportCfgs(fileNode.getFile());
			}
		});
		this.svnUpdateItem = this.createMenuItem("更新", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (jTree.getSelectionPath() == null) {
					return;
				}

				CfgPanel.FileNode fileNode = (CfgPanel.FileNode) ((DefaultMutableTreeNode) jTree.getSelectionPath().getLastPathComponent()).getUserObject();
				if (fileNode == null) {
					return;
				}
				SvnUtil.svnEvent(SvnUtil.SvnCommand.UPDATE, fileNode.getFile().getAbsolutePath());
			}
		});

		this.svnCommitItem = this.createMenuItem("提交", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (jTree.getSelectionPath() == null) {
					return;
				}

				CfgPanel.FileNode fileNode = (CfgPanel.FileNode) ((DefaultMutableTreeNode) jTree.getSelectionPath().getLastPathComponent()).getUserObject();
				if (fileNode == null) {
					return;
				}
				SvnUtil.svnEvent(SvnUtil.SvnCommand.UPDATE, fileNode.getFile().getAbsolutePath());
			}
		});
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON3) {
			return;
		}

		TreePath path = jTree.getPathForLocation(e.getX(), e.getY());
		if (path == null) {
			return;
		}
		jTree.setSelectionPath(path);

		CfgPanel.FileNode fileNode = (CfgPanel.FileNode) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
		File file = fileNode.getFile();
		JPopupMenu jPopupMenu = new JPopupMenu();
		convertItem.setText(file.isDirectory() ? "转换所有" : "转换");
		jPopupMenu.add(openItem);
		jPopupMenu.add(new JSeparator());
		jPopupMenu.add(convertItem);
		jPopupMenu.add(svnUpdateItem);
		if (OSUtil.isWindows()) {
			jPopupMenu.add(svnCommitItem);
		}
		jPopupMenu.show(jTree, e.getX(), e.getY());
	}

	private JMenuItem createMenuItem(String text, MouseListener mouseListener) {
		JMenuItem item = new JMenuItem(text);
		item.setPreferredSize(new Dimension(150, 40));
		item.setFont(UiConstant.DEFAULT_FONT);
		item.setBackground(Color.white);
		item.addMouseListener(mouseListener);
		return item;
	}

	/***
	 * 递归 循环所有的配置.
	 * @param fileOrDir
	 */
	private void exportCfgs(File fileOrDir) {
		if (fileOrDir.isDirectory()) {
			File[] listFiles = fileOrDir.listFiles();
			if (listFiles == null) {
				return;
			}
			for (File file : listFiles) {
				this.exportCfgs(file);
			}
		}else {
			if (Excel2CfgsUtil.filePostfixCheck(fileOrDir)) {
				new ExcelToCfg(fileOrDir).excelToStream();
			}
		}
	}
}
