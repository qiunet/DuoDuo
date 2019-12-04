package org.qiunet.fx.bean;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.qiunet.utils.FileUtil;

import java.io.File;

/**
 * created by wgw on 2019/9/7
 */
public class FileTreeItem extends TreeItem<FileTreeItem.FileTree> {

	public static class FileTree {
		private File file;
		private boolean isFirstChildren;


		public FileTree(File file) {
			this(file, true);
		}

		public FileTree(File file, boolean isFirstChildren) {
			this.file = file;
			this.isFirstChildren = isFirstChildren;
		}

		public File getFile() {
			return file;
		}

		@Override
		public String toString() {
			if (file == null) return "";
			if (file.isFile())
				return file.getName();
			else if(!isFirstChildren)
				return file.getName();
			return file.getPath();
		}
	}

	private boolean isLeaf;
	private boolean isFirstTimeChildren = true;
	private boolean isFirstTimeLeaf = true;

	public FileTreeItem(File file) {
		super(new FileTree(file));
	}

	public FileTreeItem(File file, boolean isFirstTimeChildren) {
		super(new FileTree(file, isFirstTimeChildren));
	}

	@Override
	public ObservableList<TreeItem<FileTree>> getChildren() {
		if (isFirstTimeChildren) {
			isFirstTimeChildren = false;
			super.getChildren().setAll(buildChildren(this));
		}
		return super.getChildren();
	}

	@Override
	public boolean isLeaf() {
		if (isFirstTimeLeaf) {
			isFirstTimeLeaf = false;
			File f = getValue().getFile();
			isLeaf = f.isFile();
		}
		return isLeaf;
	}

	private ObservableList<TreeItem<FileTree>> buildChildren(TreeItem<FileTree> TreeItem) {
		File f = TreeItem.getValue().getFile();
		if (f != null && f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null) {
				ObservableList<TreeItem<FileTree>> children = FXCollections.observableArrayList();
				for (File childFile : files) {
					if (!childFile.isHidden() && FileUtil.isExcel(childFile.getName()))
						children.add(new FileTreeItem(childFile, false));
				}
				return children;
			}
		}
		return FXCollections.emptyObservableList();
	}


}
