package org.qiunet.quartz.trigger;

import org.qiunet.quartz.CronSchedule;
import org.qiunet.utils.listener.event.data.NewDayEvent;

/***
 *
 * @author qiunet
 * 2020-04-02 08:22
 **/
class CommonTrigger {
	/**
	 * 跨天触发
	 */
	@CronSchedule("0 0 0 * * ?")
	public void newDayTrigger(){
		NewDayEvent.fireNewDayEventHandler();
	}
}
