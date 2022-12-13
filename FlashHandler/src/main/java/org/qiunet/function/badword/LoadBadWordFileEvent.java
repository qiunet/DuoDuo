package org.qiunet.function.badword;

import org.qiunet.utils.listener.event.IListenerEvent;

import java.io.File;

/***
 *
 * 加载bad word 仅支持文本文件
 * 不指定文件. 使用默认的文件
 *
 * @author qiunet
 * 2021/11/25 10:35
 */
public class LoadBadWordFileEvent implements IListenerEvent {
	/**
	 * 文件
	 */
	private File file;

	public static LoadBadWordFileEvent valueOf(File file){
		LoadBadWordFileEvent data = new LoadBadWordFileEvent();
		data.file = file;
		return data;
	}

	public File getFile() {
		return file;
	}
}
