package org.qiunet.excel2cfgs.swing.listener;

import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.common.utils.Excel2CfgsUtil;
import org.qiunet.excel2cfgs.common.utils.ExcelToCfg;
import org.qiunet.excel2cfgs.common.utils.SvnUtil;
import org.qiunet.excel2cfgs.swing.SwingUtil;
import org.qiunet.excel2cfgs.swing.panel.CfgPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/***
 * 右键操作
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
	private final JMenuItem svnLockItem;

	public JTreeMouseListener(JTree jTree) {
		this.jTree = jTree;
		this.openItem = this.createMenuItem("打开", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handlerEvent(SwingUtil::open);
			}
		});

		this.convertItem = this.createMenuItem("转换", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handlerEvent(file -> exportCfgs(file));
			}
		});

		this.svnUpdateItem = this.createMenuItem("更新", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handlerEvent(file -> SvnUtil.svnEvent(SvnUtil.SvnCommand.UPDATE, file.getAbsolutePath()));
			}
		});

		this.svnCommitItem = this.createMenuItem("提交", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handlerEvent(file -> SvnUtil.svnEvent(SvnUtil.SvnCommand.COMMIT, file.getAbsolutePath()));
			}
		});

		this.svnLockItem = this.createMenuItem("锁定", new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handlerEvent(file -> {
					if (file.canWrite()) {
						SvnUtil.svnEvent(SvnUtil.SvnCommand.UNLOCK, file.getAbsolutePath());
						svnLockItem.setText("锁定");
					}else {
						SvnUtil.svnEvent(SvnUtil.SvnCommand.LOCK, file.getAbsolutePath());
						svnLockItem.setText("解锁");
					}
				});
			}
		});
	}

	private void handlerEvent(IFileOperation runnable) {
		if (jTree.getSelectionPath() == null) {
			return;
		}

		CfgPanel.FileNode fileNode = (CfgPanel.FileNode) ((DefaultMutableTreeNode) jTree.getSelectionPath().getLastPathComponent()).getUserObject();
		if (fileNode == null) {
			return;
		}
		runnable.run(fileNode.getFile());
	}
	@FunctionalInterface
	interface IFileOperation {
		void run(File file);
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
		jPopupMenu.add(svnCommitItem);
		jPopupMenu.add(svnLockItem);
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
