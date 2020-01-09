package org.qiunet.frame.component;

import javafx.scene.control.TreeItem;
import org.qiunet.utils.Excel2CfgsUtil;

import java.io.File;

/***
 *
 *
 * @author  qiunet
 * 2019-11-07 17:44
 ***/
public class FileTreeItem extends TreeItem<String> {
	private File file;

	public FileTreeItem(File file) {
		super(file.getName());
		this.file = file;

		if (file.isDirectory()) {
			this.fillChildren(this);
		}

		this.setExpanded(true);
	}

	public File getFile() {
		return file;
	}

	@Override
	public boolean isLeaf() {
		return this.file.isFile();
	}

	private void fillChildren(FileTreeItem parent) {
		if (! file.isDirectory()) return;

		for (File file : parent.file.listFiles()) {
			if (! Excel2CfgsUtil.filePostfixCheck(file)) continue;

			parent.getChildren().add(new FileTreeItem(file));
		}
	}
}
