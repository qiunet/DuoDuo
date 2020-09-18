package org.qiunet.utils.file;

import org.qiunet.utils.async.factory.DefaultThreadFactory;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class FileLoader {
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("FileLoader_"));
	private static final FileChangeMonitor monitor = new FileChangeMonitor();
	static {
		executor.scheduleAtFixedRate(monitor, 1, 10, TimeUnit.SECONDS);
	}
	private FileLoader(){}
	/***
	 * 指定的文件 或者文件夹变动时候, 会调用callback的call方法. 如果是传入的文件夹, 需要对file进行判断
	 * @param file
	 * @param callback
	 */
	public static void listener(File file, IFileChangeCallback callback) {
		monitor.addObserver(file, callback);
	}
}
