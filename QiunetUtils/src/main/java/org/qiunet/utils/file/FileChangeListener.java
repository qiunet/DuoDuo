package org.qiunet.utils.file;

import org.qiunet.utils.timer.TimerManager;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class FileChangeListener {
	private static final FileChangeMonitor monitor = new FileChangeMonitor();
	static {
		TimerManager.executor.scheduleAtFixedRate(monitor, 1, 10, TimeUnit.SECONDS);
	}
	private FileChangeListener(){
	}
	/***
	 * 指定的文件 或者文件夹变动时候, 会调用callback的call方法. 如果是传入的文件夹, 需要对file进行判断
	 * @param file
	 * @param callback
	 */
	public static void listener(File file, IFileChangeCallback callback) {
		monitor.addObserver(file, callback);
	}
}
