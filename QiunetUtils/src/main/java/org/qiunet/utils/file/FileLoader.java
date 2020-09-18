package org.qiunet.utils.file;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public final class FileLoader {
	private static Logger logger = LoggerType.DUODUO.getLogger();
	private static final List<FileAlterationObserver> monitor = new CopyOnWriteArrayList<>();
	static {
		TimerManager.getInstance().scheduleAtFixedRate(() -> monitor.forEach(FileAlterationObserver::checkAndNotify)
			, 1, 10, TimeUnit.SECONDS);
	}
	private FileLoader(){}
	/***
	 * 指定的文件 或者文件夹变动时候, 会调用callback的call方法. 如果是传入的文件夹, 需要对file进行判断
	 * @param file
	 * @param callback
	 */
	public static void listener(File file, IFileChangeCallback callback) {
		FileAlterationObserver observer;
		if (file.isDirectory()) {
			observer = new FileAlterationObserver(file);
		}else {
			observer = new FileAlterationObserver(file.getParent(), f -> file.getAbsolutePath().equals(f.getAbsolutePath()));
		}

		observer.addListener(new FileAlterationListenerAdaptor() {
			@Override
			public void onFileChange(File file) {
				try {
					callback.call(file);
				}catch (Exception e) {
					logger.error("FileLoader Exception: ", e);
				}
			}
		});
		monitor.add(observer);
	}
}
