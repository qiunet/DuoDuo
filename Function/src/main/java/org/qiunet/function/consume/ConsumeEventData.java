package org.qiunet.function.consume;

import org.qiunet.listener.event.IEventData;

/***
 * 消耗事件
 *
 * @author qiunet
 * 2021-01-05 20:52
 */
public class ConsumeEventData implements IEventData {

	private ConsumeContext context;

	public static ConsumeEventData valueOf(ConsumeContext context) {
		ConsumeEventData data = new ConsumeEventData();
		data.context = context;
		return data;
	}

	public ConsumeContext getContext() {
		return context;
	}
}
