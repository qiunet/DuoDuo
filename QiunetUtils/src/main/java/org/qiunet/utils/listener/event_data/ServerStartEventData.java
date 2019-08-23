package org.qiunet.utils.listener.event_data;

import org.qiunet.utils.listener.IEventData;

/***
 * 服务启动
 */
public final class ServerStartEventData implements IEventData {

	private ServerStartEventData(){}

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final ServerStartEventData instance = new ServerStartEventData();

	public static void fireStartEventHandler(){
		instance.fireEventHandler();
	}
}
