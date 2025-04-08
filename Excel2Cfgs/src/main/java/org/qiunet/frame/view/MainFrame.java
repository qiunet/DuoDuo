package org.qiunet.frame.view;

import org.qiunet.frame.base.BaseJFrame;
import org.qiunet.frame.base.JframeManager;
import org.qiunet.utils.ExcelToStream;
import org.qiunet.utils.FileUtil;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainFrame extends BaseJFrame {
	public MainFrame() {
	}
	/**
	 *
	 */
	private JScrollPane scrollPane;

	private JTree jTree;

	private JPopupMenu popupMenu;

	private JPopupMenu dirPopupMenu;

	private static final long serialVersionUID = -4680070900448216352L;

	private void jframeInit(){
		this.setSize(400, 600);
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

		JMenu refresh = new JMenu("刷新");
		JMenuItem subRefreshMenu = new JMenuItem("刷新文件");
		subRefreshMenu.addActionListener(new RefreshMenuAction());
		refresh.add(subRefreshMenu);
		menuBar.add(refresh);

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
			frame.popDirMenuInit();
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
		JMenuItem exchange = new JMenuItem("转换");
		exchange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
				if (((FileNode)node.getUserObject()).getFile().isDirectory()) return;

				FileNode fileNode = (FileNode) node.getUserObject();
				if (fileNode.getFile().getName().endsWith(".xlsx") || fileNode.getFile().getName().endsWith(".xls")) {
					ExcelToStream excelToStream = new ExcelToStream();
					String msg = excelToStream.excelToStream(fileNode.file);
					if (msg != null){
						JOptionPane.showMessageDialog(scrollPane, "错误:["+msg+"]");
					}
					JOptionPane.showMessageDialog(scrollPane, "SUCCESS");
					JframeManager.getInstance().getJframe(MainFrame.class).loadFileTree();
				}
			}
		});
		popupMenu.add(exchange);
	}

	/***
	 * 右键点击分类导航树的菜单
	 */
	private void popDirMenuInit() {
		dirPopupMenu = new JPopupMenu();
		JMenuItem exchange = new JMenuItem("转换文件夹内所有");
		exchange.addActionListener(new ActionListener() {
			private ExcelToStream excelToStream = new ExcelToStream();
			private void exchangeAll(File dir, Set<String> errorFiles){
				File [] files = dir.listFiles();
				if (files == null) return;

				for (File file : files) {
					if (! filePostfixCheck(file) || file.getName().endsWith(".xd") || file.getName().endsWith(".json")) continue;

					System.out.println(file.getName());

					if (file.isFile()) {
						String msg = excelToStream.excelToStream(file);
						if (msg != null) {
							errorFiles.add(file.getName());
							JOptionPane.showMessageDialog(scrollPane, "错误:["+msg+"]");
							return;
						}
						JframeManager.getInstance().getJframe(MainFrame.class).loadFileTree();
					}else {
						exchangeAll(file, errorFiles);
					}
				}

			}
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
				if (!((FileNode)node.getUserObject()).getFile().isDirectory()) return;

				FileNode fileNode = (FileNode) node.getUserObject();
				Set<String> errorFiles = new HashSet<>();
				this.exchangeAll(fileNode.file, errorFiles);
				if (errorFiles.isEmpty()) {
					// 重新加载树
					JframeManager.getInstance().getJframe(MainFrame.class).loadFileTree();

					JOptionPane.showMessageDialog(scrollPane, "SUCCESS");
				}
			}
		});
		dirPopupMenu.add(exchange);
	}

	/***
	 * 加载文件树
	 */
	private void loadFileTree () {
		String workPath = FileUtil.returnPathFromProjectFile();
		if (workPath == null || workPath.length() == 0 || !new File(workPath).exists()) return;

		File workPathFile = new File(workPath);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FileNode(workPathFile));
		this.loadTree(root, workPathFile);
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
			this.scrollPane.setViewportView(jTree);
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

	private Set<String> expandFileName = new HashSet<>();

	private static final Set<String> postfixs = new HashSet(Arrays.asList(new String[]{"xlsx","xls","xd","json"}));

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
				if (! postfixs.contains(postfix)) return false;
			}
		}
		return true;
	}
	/***
	 * 递归加载树
	 * @param dirRoot
	 * @param dirFile
	 */
	private void loadTree(DefaultMutableTreeNode dirRoot, File dirFile) {
		File [] files = dirFile.listFiles();
		if (files == null) return;

		for (File file : files){
			if (! filePostfixCheck(file)) continue;

			if (file.isFile()) {
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
			jFileChooser.setDialogTitle("请选择你的Excel配置文件夹");
			jFileChooser.setApproveButtonText("选定文件夹");
			jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (jFileChooser.showOpenDialog(JframeManager.getInstance().getJframe(MainFrame.class)) == JFileChooser.APPROVE_OPTION){
				System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
				FileUtil.writeToProjectFile(jFileChooser.getSelectedFile());
				JframeManager.getInstance().getJframe(MainFrame.class).loadFileTree();
			}
		}
	}

	public static final class RefreshMenuAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JframeManager.getInstance().getJframe(MainFrame.class).loadFileTree();
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


	class TreePopMenuListener extends MouseAdapter {

		public void mousePressed(MouseEvent e) {


			TreePath path = jTree.getPathForLocation(e.getX(), e.getY());
			if (path == null) return;

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			FileNode fileNode = (FileNode) node.getUserObject();
			if (e.getModifiers() == 16 && e.getClickCount() == 2) {
				// 左键
				this.openFile(fileNode);
			} else if (e.isMetaDown()) {
				// 右键
				this.showMenu(fileNode, e, path);
			}
		}
		private void openFile(FileNode fileNode){
			try {
				Desktop.getDesktop().open(fileNode.file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		private void showMenu(FileNode fileNode, MouseEvent e, TreePath path){
			if (! fileNode.getFile().isDirectory()) {
				if (fileNode.getFile().getName().endsWith(".xlsx") || fileNode.getFile().getName().endsWith(".xls")) {
					jTree.setSelectionPath(path);
					popupMenu.show(jTree, e.getX(), e.getY());
				}
			}else {
				if (e.getButton() == MouseEvent.BUTTON3){
					jTree.setSelectionPath(path);
					dirPopupMenu.show(jTree, e.getX(), e.getY());
				}
			}
		}
	}
}
