package org.qiunet.utils.listener.event.data;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 服务终止事件
 * 在ServerShutdown后执行
 *
 * @author qiunet
 * 2020-09-05 06:07
 **/
public class ServerStoppedEvent implements IListenerEvent {

		private ServerStoppedEvent(){}

		/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
		private static final ServerStoppedEvent instance = new ServerStoppedEvent();

		public static void fireEvent(){
			instance.fireEventHandler();
		}
}
