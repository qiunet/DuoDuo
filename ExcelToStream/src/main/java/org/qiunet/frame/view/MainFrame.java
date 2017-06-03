package org.qiunet.frame.view;

import org.qiunet.frame.base.BaseJFrame;
import org.qiunet.frame.base.JframeManager;
import org.qiunet.utils.ExcelToStream;
import org.qiunet.utils.FileUtil;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.util.*;

public class MainFrame extends BaseJFrame {
	/**
	 *
	 */
	private JScrollPane scrollPane;

	private JTree jTree;

	private JPopupMenu popupMenu;

	private static final long serialVersionUID = -4680070900448216352L;

	private static final String loadWorkHomeName = ".xd.project";
	private void jframeInit(){
		this.setSize(200, 600);
		this.setTitle("设定转换工具");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
	}

	public void dataInit() {
		scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		setContentPane(scrollPane);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("设置");
		JMenuItem subMenu = new JMenuItem("Excel路径");
		subMenu.addActionListener(new MenuAction());
		menu.add(subMenu);
		menuBar.add(menu);

		this.setJMenuBar(menuBar);
		this.loadFileTree();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MainFrame frame = JframeManager.getInstance().getJframe(MainFrame.class);
			frame.jframeInit();
			frame.popMenuInit();
			frame.dataInit();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 右键点击分类导航树的菜单
	 */
	private void popMenuInit() {
		popupMenu = new JPopupMenu();
		JMenuItem exchange = new JMenuItem("转换xd");
		exchange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
				if (((FileNode)node.getUserObject()).getFile().isDirectory()) return;

				FileNode fileNode = (FileNode) node.getUserObject();
				if (fileNode.getFile().getName().endsWith(".xlsx") || fileNode.getFile().getName().endsWith(".xls")) {
					ExcelToStream excelToStream = new ExcelToStream();
					String msg = excelToStream.excelToStream(fileNode.file);
					if (msg != null)
						JOptionPane.showMessageDialog(scrollPane, "错误:["+msg+"]");

					JframeManager.getInstance().getJframe(MainFrame.class).loadFileTree();
				}
			}
		});
		popupMenu.add(exchange);
	}

	/***
	 * 加载文件树
	 */
	private void loadFileTree () {
		String workPath = FileUtil.returnPathFromProjectFile(getFileName());
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
		if (workPath != null && workPath.trim().length() != 0) {
			File workPathFile = new File(workPath);
			this.loadTree(root, workPathFile);
		}
		if (jTree != null) {
			this.recordExpandNode(new TreePath(jTree.getModel().getRoot()));
		}

		if (this.jTree != null) {
			this.jTree.removeAll();
			this.jTree.setModel(new DefaultTreeModel(root));
			this.jTree.repaint();
		} else {
			this.jTree = new JTree(root);
			jTree.addMouseListener(new TreePopMenuListener());
			jTree.setExpandsSelectedPaths(true);
			jTree.setScrollsOnExpand(true);
			this.scrollPane.getViewport().add(jTree);
		}
		this.revertExpandNode(new TreePath(jTree.getModel().getRoot()));
	}

	/**
	 * 恢复展开状态
	 * @param treePath
	 */
	private void revertExpandNode(TreePath treePath) {
		if (expandFileName.isEmpty()) return;

		for (Enumeration<DefaultMutableTreeNode> it = ((TreeNode)treePath.getLastPathComponent()).children(); it.hasMoreElements(); ){
			DefaultMutableTreeNode tn  = it.nextElement();
			if (! ((FileNode)tn.getUserObject()).file.isDirectory()) continue;

			if (expandFileName.contains(((FileNode)tn.getUserObject()).file.getAbsolutePath())) {
				TreePath subPath = treePath.pathByAddingChild(tn);
				jTree.expandPath(subPath);
				revertExpandNode(subPath);
			}
		}
		this.expandFileName.clear();
	}

	/***
	 * 记录展开状态
	 * @param treePath
	 */
	private void recordExpandNode(TreePath treePath){
		if (jTree == null) return;

		for (Enumeration<DefaultMutableTreeNode> it = ((TreeNode)treePath.getLastPathComponent()).children(); it.hasMoreElements(); ){
			DefaultMutableTreeNode tn  = it.nextElement();
			if (! ((FileNode)tn.getUserObject()).file.isDirectory()) continue;

			TreePath subPath = treePath.pathByAddingChild(tn);
			if (jTree.isExpanded(subPath)){
				expandFileName.add(((FileNode)tn.getUserObject()).file.getAbsolutePath());
				this.recordExpandNode(subPath);
			}
		}
	}

	private Set<String> expandFileName = new HashSet<String>();

	private static final Set<String> postfixs = new HashSet<String>(Arrays.asList(new String[]{"xlsx","xls","xd"}));
	/***
	 * 递归加载树
	 * @param dirRoot
	 * @param dirFile
	 */
	private void loadTree(DefaultMutableTreeNode dirRoot, File dirFile) {
		for (File file : dirFile.listFiles()){
			if (file.getName().startsWith("~") || file.getName().startsWith(".")) continue;

			if (file.isFile()) {
				if (file.getName().contains(".")) {
					String postfix = file.getName().substring(file.getName().indexOf(".")+1, file.getName().length());
					if (! postfixs.contains(postfix)) continue;
				}

				dirRoot.add(new DefaultMutableTreeNode(new FileNode(file), false));
			}else if (file.isDirectory()) {
				DefaultMutableTreeNode dir = new DefaultMutableTreeNode(new FileNode(file));
				dirRoot.add(dir);
				this.loadTree(dir, file);
			}
		}
	}

	/****
	 * 菜单的事件
	 * @author qiunet
	 */
	public static final class MenuAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setDialogTitle("请选择你的Excel目录");
			jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (jFileChooser.showOpenDialog(JframeManager.getInstance().getJframe(MainFrame.class)) == JFileChooser.APPROVE_OPTION){
				System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
				FileUtil.writeToProjectFile(jFileChooser.getSelectedFile().getAbsolutePath(), getFileName());
				JframeManager.getInstance().getJframe(MainFrame.class).loadFileTree();
			}
		}
	}

	public static class FileNode{
		private File file ;
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

	private static String getFileName(){
		return System.getProperty("user.dir") + File.separator + loadWorkHomeName;
	}

	class TreePopMenuListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON3) return;

			TreePath path = jTree.getPathForLocation(e.getX(), e.getY());
			if (path == null) return;

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			if (((FileNode)node.getUserObject()).getFile().isDirectory()) return;

			FileNode fileNode = (FileNode) node.getUserObject();
			if (fileNode.getFile().getName().endsWith(".xlsx") || fileNode.getFile().getName().endsWith(".xls")) {
				jTree.setSelectionPath(path);
				popupMenu.show(jTree, e.getX(), e.getY());
			}
		}
		public void mouseReleased(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
	}
}
