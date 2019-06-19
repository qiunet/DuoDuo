package org.qiunet.utils.listener;

public class ListenerManager {
	private volatile static ListenerManager instance;

	private ListenerManager() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static ListenerManager getInstance() {
		if (instance == null) {
			synchronized (ListenerManager.class) {
				if (instance == null)
				{
					new ListenerManager();
				}
			}
		}
		return instance;
	}

	/***
	 * 触发监听处理
	 * @param data
	 */
	public <Data extends IEventData> void fireEventHandler(Data data) {
		ListenerManager0.getInstance().fireEventHandler(data);
	}
}
