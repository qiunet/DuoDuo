package org.qiunet.data.core.support.db.event;

import org.qiunet.listener.event.IEventData;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 18:43
 */
public class DbLoaderOverEventData implements IEventData {

	private static DbLoaderOverEventData instance = new DbLoaderOverEventData();

	public static void fireEvent() {
		instance.fireEventHandler();
	}
}
