package org.qiunet.excel2cfgs.frame.component;

import javafx.scene.control.TreeItem;
import org.qiunet.excel2cfgs.utils.Excel2CfgsUtil;

import java.io.File;

/***
 *
 *
 * @author  qiunet
 * 2019-11-07 17:44
 ***/
public class FileTreeItem extends TreeItem<File> {

	public FileTreeItem(File file) {
		super(file);

		if (file.isDirectory()) {
			this.fillChildren(this);
		}
		this.setExpanded(true);
	}

	@Override
	public boolean isLeaf() {
		return getValue().isFile();
	}

	private void fillChildren(FileTreeItem parent) {
		if (! getValue().isDirectory()) {
			return;
		}

		for (File file : parent.getValue().listFiles()) {
			if (! Excel2CfgsUtil.filePostfixCheck(file)) {
				continue;
			}

			parent.getChildren().add(new FileTreeItem(file));
		}
	}

	@Override
	public String toString() {
		return getValue().getName();
	}
}
