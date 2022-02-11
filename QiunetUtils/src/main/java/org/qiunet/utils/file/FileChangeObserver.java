package org.qiunet.utils.file;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

/***
 *
 *
 * @author qiunet
 * 2020-09-18 12:27
 */
class FileChangeObserver {
	/**
	 * 需要监听的文件
	 */
	private final File file;
	/**
	 * 长度
	 */
	private long length;
	/**
	 * 最后更改时间
	 */
	private long lastModified;
	/**
	 * 有变动需要通知的callback
	 */
	private final List<IFileChangeCallback> changeCallbacks;

	FileChangeObserver(File file) {
		this.changeCallbacks = Lists.newArrayListWithCapacity(3);
		this.lastModified = file.lastModified();
		this.length = file.length();
		this.file = file;
	}

	String getName(){
		return getFile().toString();
	}

	File getFile() {
		return file;
	}

	synchronized void addCallback(IFileChangeCallback changeCallback) {
		this.changeCallbacks.add(changeCallback);
	}
	/**
	 * 检查然后通知
	 */
	void checkAndNotify(){
		long lastModify = file.lastModified();
		long len = file.length();

		if (this.lastModified != lastModify || this.length != len) {
			synchronized (this) {
				changeCallbacks.forEach(changeCallback -> changeCallback.call(file));
			}
		}

		this.lastModified = lastModify;
		this.length = len;
	}

	@Override
	public String toString() {
		return "FileChangeObserver{" +
				"file=" + file +
				", length=" + length +
				", lastModified=" + lastModified +
				'}';
	}
}
