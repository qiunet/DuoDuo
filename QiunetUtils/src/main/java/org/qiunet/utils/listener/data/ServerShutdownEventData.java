package org.qiunet.utils.listener.data;

import org.qiunet.utils.listener.EventListener;
import org.qiunet.utils.listener.IEventData;

/***
 *
 * 服务器启动的监听数据
 * @author qiunet
 */
@EventListener(ServerShutdownEventData.ServerShutdownListener.class)
public class ServerShutdownEventData implements IEventData {

	private ServerShutdownEventData(){}

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final ServerShutdownEventData instance = new ServerShutdownEventData();

	public static void fireShutdownEventHandler(){
		instance.fireEventHandler();
	}

	public interface ServerShutdownListener {

		void onShutdown(ServerShutdownEventData data);
	}
}
