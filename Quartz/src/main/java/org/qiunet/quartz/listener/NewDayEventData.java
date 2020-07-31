package org.qiunet.quartz.listener;

import org.qiunet.utils.listener.EventListener;
import org.qiunet.utils.listener.IEventData;

/***
 * 跨天的监听
 * 每日零点定时触发.
 *
 * @author qiunet
 * 2020-04-02 08:19
 **/
@EventListener(NewDayEventData.NewDayListener.class)
public class NewDayEventData implements IEventData {

	public interface NewDayListener {

		void newDay(NewDayEventData data);
	}
}
