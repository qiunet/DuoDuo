package org.qiunet.utils.listener.event.data;

import org.qiunet.utils.listener.event.IListenerEvent;
import org.qiunet.utils.logger.LoggerType;

/***
 * 服务停止事件.
 * 属于通用. DuoDuo需要监听.
 *
 * @author qiunet
 * 2020-09-05 06:07
 **/
public class ServerShutdownEvent implements IListenerEvent {

		private ServerShutdownEvent(){}

		/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
		private static final ServerShutdownEvent instance = new ServerShutdownEvent();

		public static void fireShutdownEventHandler(){
			instance.fireEventHandler((m, e) -> {
				LoggerType.DUODUO.error("Server shutdown exception:", e);
			});
		}
}
