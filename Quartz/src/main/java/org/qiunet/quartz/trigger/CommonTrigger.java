package org.qiunet.quartz.trigger;

import org.qiunet.listener.event.data.NewDayEventData;
import org.qiunet.quartz.CronSchedule;

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
		NewDayEventData.fireNewDayEventHandler();
	}
}
