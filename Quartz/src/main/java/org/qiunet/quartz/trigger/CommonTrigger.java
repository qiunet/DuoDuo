package org.qiunet.quartz.trigger;

import org.qiunet.quartz.CronSchedule;
import org.qiunet.quartz.listener.NewDayEventData;

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
		new NewDayEventData().fireEventHandler();
	}
}
