package org.qiunet.utils.listener.data;

import org.qiunet.utils.listener.EventListener;
import org.qiunet.utils.listener.IEventData;

/***
 * 服务启动
 * @author qiunet
 */
@EventListener(ServerStartUpEventData.ServerStartUpListener.class)
public final class ServerStartUpEventData implements IEventData {

	private ServerStartUpEventData(){}

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final ServerStartUpEventData instance = new ServerStartUpEventData();

	public static void fireStartEventHandler(){
		instance.fireEventHandler();
	}

	public interface ServerStartUpListener {
		void onServerStartUp(ServerStartUpEventData data);
	}
}
