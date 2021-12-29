package org.qiunet.flash.handler.netty.server.event;

import org.qiunet.utils.listener.event.IEventData;

/***
 *
 * @author qiunet
 * 2021/12/29 13:55
 */
public class ServerStartupCompleteEvent implements IEventData {

	private ServerStartupCompleteEvent(){}

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final ServerStartupCompleteEvent instance = new ServerStartupCompleteEvent();

	public static void fireStartupCompleteEvent(){
		instance.fireEventHandler();
	}
}
