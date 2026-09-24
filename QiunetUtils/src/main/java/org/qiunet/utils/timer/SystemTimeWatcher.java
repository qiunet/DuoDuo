package org.qiunet.utils.timer;

import org.qiunet.utils.date.DateUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 仅监听 {@link DateUtil} 逻辑时间偏移变化 ({@link DateUtil#setTimeOffset} /
 * {@link DateUtil#clearTimeOffset}), 通知调度容器重算已有延迟任务.
 * <p>
 * 不探测 OS 系统时钟跳变; 测试通过工具改偏移即可触发到期调度.
 */
public enum SystemTimeWatcher {
	instance;

	private final List<Runnable> listeners = new CopyOnWriteArrayList<>();

	SystemTimeWatcher() {
		DateUtil.addTimeChangeListener(this::notifyListeners);
	}

	/**
	 * 注册逻辑时间变化回调
	 */
	public void addListener(Runnable listener) {
		listeners.add(listener);
	}

	/**
	 * 移除回调
	 */
	public void removeListener(Runnable listener) {
		listeners.remove(listener);
	}

	private void notifyListeners() {
		for (Runnable listener : listeners) {
			try {
				listener.run();
			} catch (Throwable ignored) {
				// 单个监听失败不影响其它
			}
		}
	}
}
