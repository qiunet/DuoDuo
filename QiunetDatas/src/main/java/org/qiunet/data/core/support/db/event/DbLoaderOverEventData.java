package org.qiunet.data.core.support.db.event;

import org.qiunet.utils.listener.event.IEventData;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 18:43
 */
public class DbLoaderOverEventData implements IEventData {

	private static final DbLoaderOverEventData instance = new DbLoaderOverEventData();

	public static void fireEvent() {
		instance.fireEventHandler();
	}
}
