package org.qiunet.utils.listener.event.data;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 服务器被弃用通知
 *
 * @author qiunet
 * 2021/12/8 13:59
 */
public class ServerDeprecatedEvent implements IListenerEvent {
	private ServerDeprecatedEvent(){}

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final ServerDeprecatedEvent instance = new ServerDeprecatedEvent();

	public static void fireDeprecated(){
		instance.fireEventHandler();
	}
}
