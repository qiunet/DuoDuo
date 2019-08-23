package org.qiunet.utils.listener.event_data;

import org.qiunet.utils.listener.IEventData;

public class ServerShutdownEventData implements IEventData {

	private ServerShutdownEventData(){}

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final ServerShutdownEventData instance = new ServerShutdownEventData();

	public static void fireShutdownEventHandler(){
		instance.fireEventHandler();
	}
}
