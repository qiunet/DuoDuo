package org.qiunet.excel2cfgs.swing.panel;

import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.utils.SvnUtil;
import org.qiunet.utils.system.OSUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

/***
 * 配置转换的面板
 *
 * @Author qiunet
 * @Date 2021/2/9 21:56
 **/
public class CfgPanel extends IconJPanel {

	private JPanel showPanel;
	private JLabel currExcelPath;
	private JLabel currProCfgPath;
	private JLabel currProCfgPathNameLabel;
	private JTree excelPathTree;
	private JTextArea console;
	private JButton refreshBtn;
	private JButton svnUpdate;
	private JButton svnCommit;
	private JButton svnClean;


	public CfgPanel() {
		if (OSUtil.isLinux() || OSUtil.isMac()) {
			svnCommit.setVisible(false);
		}

		refreshBtn.addActionListener(e -> {

		});

		svnClean.addActionListener(e -> {
			String path = SettingManager.getInstance().getFirstExcelPath();
			SvnUtil.svnEvent(SvnUtil.SvnCommand.CLEANUP, path);
		});
		svnCommit.addActionListener(e -> {
			String path = SettingManager.getInstance().getFirstExcelPath();
			SvnUtil.svnEvent(SvnUtil.SvnCommand.COMMIT, path);
		});
		svnUpdate.addActionListener(e -> {
			String path = SettingManager.getInstance().getFirstExcelPath();
			SvnUtil.svnEvent(SvnUtil.SvnCommand.UPDATE, path);
		});
	}

	@Override
	public void unActivate() {

	}

	@Override
	public void activate() {
		this.currProCfgPathNameLabel.setVisible(SettingManager.getInstance().getSetting().getRoleType() != RoleType.SCHEMER);
		this.currProCfgPath.setVisible(SettingManager.getInstance().getSetting().getRoleType() != RoleType.SCHEMER);
		this.currProCfgPath.setText(SettingManager.getInstance().getFirstCfgPath());
		this.currExcelPath.setText(SettingManager.getInstance().getFirstExcelPath());
	}

	@Override
	public IconButtonType type(){
		return IconButtonType.cfg;
	}

	@Override
	public JPanel getShowPanel() {
		return showPanel;
	}

	@Override
	public void addToParent(ToolTabPanel toolTabPanel) {
		toolTabPanel.getPanelUp().add(this.getButton());
	}

	/***
	 * 加载文件树
	 */
	private void loadFileTree () {
		String workPath = SettingManager.getInstance().getFirstExcelPath();
		if (workPath == null || workPath.length() == 0 || !new File(workPath).exists()) return;

		File workPathFile = new File(workPath);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FileNode(workPathFile));
//		this.loadTree(root, workPathFile);
//		if (excelPathTree != null) {
//			this.recordExpandNode(new TreePath(excelPathTree.getModel().getRoot()));
//		}
//
//		if (this.excelPathTree != null) {
//			this.excelPathTree.removeAll();
//			this.excelPathTree.setModel(new DefaultTreeModel(root));
//			this.excelPathTree.repaint();
//		} else {
//			this.excelPathTree = new JTree(root);
//			excelPathTree.addMouseListener(new TreePopMenuListener());
//			excelPathTree.setExpandsSelectedPaths(true);
//			excelPathTree.setScrollsOnExpand(true);
//			this.scrollPane.setViewportView(jTree);
//		}
//		this.revertExpandNode(new TreePath(jTree.getModel().getRoot()));
	}

	public static class FileNode{
		private final File file ;
		public FileNode (File file) {
			this.file = file;
		}

		public File getFile() {
			return file;
		}

		@Override
		public String toString() {
			return file.getName();
		}
	}
}
