package org.qiunet.excel2cfgs.swing.panel;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.qiunet.excel2cfgs.common.constants.UiConstant;
import org.qiunet.excel2cfgs.enums.RoleType;
import org.qiunet.excel2cfgs.setting.SettingManager;
import org.qiunet.excel2cfgs.swing.component.IconJPanel;
import org.qiunet.excel2cfgs.swing.enums.IconButtonType;
import org.qiunet.excel2cfgs.swing.listener.JTreeMouseListener;
import org.qiunet.excel2cfgs.utils.SvnUtil;
import org.qiunet.utils.system.OSUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/***
 * 配置转换的面板
 *
 * @Author qiunet
 * @Date 2021/2/9 21:56
 **/
public class CfgPanel extends IconJPanel {
	/**缓存*/
	private static final Map<String, DefaultMutableTreeNode> treeNodeCache = Maps.newHashMap();

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
	private DefaultMutableTreeNode root;
	public CfgPanel() {
		if (OSUtil.isLinux() || OSUtil.isMac()) {
			svnCommit.setVisible(false);
		}

		refreshBtn.addActionListener(e -> this.loadFileTree());
		TitledBorder titledBorder = new TitledBorder("控制台");
		titledBorder.setTitleColor(Color.WHITE);
		titledBorder.setTitleFont(UiConstant.DEFAULT_FONT);
		console.setBorder(titledBorder);

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
			this.loadFileTree();
		});

		excelPathTree.setScrollsOnExpand(true);
		excelPathTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 2) {
					File file = ((FileNode) ((DefaultMutableTreeNode) excelPathTree.getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject()).getFile();
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		excelPathTree.addMouseListener(new JTreeMouseListener(excelPathTree));
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

		String workPath = SettingManager.getInstance().getFirstExcelPath();
		if (workPath == null || workPath.length() == 0 || !new File(workPath).exists()) return;

		treeNodeCache.clear();
		File workPathFile = new File(workPath);
		root = new DefaultMutableTreeNode(new FileNode(workPathFile));
		this.loadFileTree();
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
		root.removeAllChildren();
		this.loadTree(root);
		this.excelPathTree.setModel(new DefaultTreeModel(root));
		this.excelPathTree.repaint();
	}


	/***
	 * 递归加载树
	 * @param dirRoot
	 */
	private void loadTree(DefaultMutableTreeNode dirRoot) {
		File dirFile = ((FileNode) dirRoot.getUserObject()).getFile();
		File [] files = dirFile.listFiles();
		if (files == null) return;

		for (File file : files){
			if (! filePostfixCheck(file)) continue;
			DefaultMutableTreeNode node = treeNodeCache.computeIfAbsent(file.getAbsolutePath(),
					key -> new DefaultMutableTreeNode(new FileNode(file), file.isDirectory())
			);

			dirRoot.add(node);

			if (file.isDirectory()) {
				this.loadTree(node);
			}
		}
	}
	private static final Set<String> postfixs = Sets.newHashSet("xlsx", "xls", "xd", "xml", "json");

	/**
	 * 校验文件的后缀名
	 * @param file 校验的文件
	 * @return true 符合
	 */
	public static final boolean filePostfixCheck(File file) {
		String fileName = file.getName();

		if (fileName.startsWith("~") || fileName.startsWith(".")) return false;

		if (file.isFile()) {
			if (file.getName().contains(".")) {
				String postfix = file.getName().substring(file.getName().indexOf(".")+1);
				return postfixs.contains(postfix);
			}
		}
		return true;
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

	public JTextArea getConsole() {
		return console;
	}
}
