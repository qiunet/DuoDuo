package org.qiunet.utils.file;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.qiunet.utils.timer.IScheduledTask;

import java.io.File;
import java.util.Map;

/***
 * 文件变动监听
 *
 * @author qiunet
 * 2020-09-18 12:33
 */
class FileChangeMonitor implements IScheduledTask {
	/**
	 * 观察者map
	 */
	private Map<String, FileChangeObserver> observerMap = Maps.newConcurrentMap();

	void addObserver(File file, IFileChangeCallback callback) {
		Preconditions.checkState(file != null && file.exists() && file.isFile());
		FileChangeObserver fileChangeObserver = observerMap.computeIfAbsent(file.toString(), key -> new FileChangeObserver(file));
		fileChangeObserver.addCallback(callback);
	}

	@Override
	public void run0() {
		this.observerMap.values().forEach(FileChangeObserver::checkAndNotify);
	}
}
