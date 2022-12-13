package org.qiunet.utils.listener.event.data;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 跨天的监听
 * 每日零点定时触发.
 *
 * @author qiunet
 * 2020-04-02 08:19
 **/
public class NewDayEvent implements IListenerEvent {

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final NewDayEvent instance = new NewDayEvent();

	public static void fireNewDayEventHandler(){
		instance.fireEventHandler();
	}
}
