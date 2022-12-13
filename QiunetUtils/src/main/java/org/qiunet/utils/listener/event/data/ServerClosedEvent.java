package org.qiunet.utils.listener.event.data;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 服务器关闭通知
 *
 * @author qiunet
 * 2021/12/8 13:59
 */
public class ServerClosedEvent implements IListenerEvent {
	private ServerClosedEvent(){}

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final ServerClosedEvent instance = new ServerClosedEvent();

	public static void fireClosed(){
		instance.fireEventHandler();
	}
}
