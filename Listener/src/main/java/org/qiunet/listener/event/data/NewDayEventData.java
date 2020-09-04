package org.qiunet.listener.event.data;

import org.qiunet.listener.event.IEventData;

/***
 * 跨天的监听
 * 每日零点定时触发.
 *
 * @author qiunet
 * 2020-04-02 08:19
 **/
public class NewDayEventData implements IEventData {

	/***因为没有参数. 所以可以使用单例 . 有参数的eventData 还是得自己new */
	private static final NewDayEventData instance = new NewDayEventData();

	public static void fireNewDayEventHandler(){
		instance.fireEventHandler();
	}
}
