package org.qiunet.function.item_change.consume;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 消耗事件
 *
 * @author qiunet
 * 2021-01-05 20:52
 */
public class ConsumeEvent implements IListenerEvent {

	private ConsumeContext context;

	public static ConsumeEvent valueOf(ConsumeContext context) {
		ConsumeEvent data = new ConsumeEvent();
		data.context = context;
		return data;
	}

	public ConsumeContext getContext() {
		return context;
	}
}
