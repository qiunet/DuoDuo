package org.qiunet.data.core.support.db.event;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 *
 *
 * @author qiunet
 * 2020-09-25 18:43
 */
public class DbLoaderOverEvent implements IListenerEvent {

	private static final DbLoaderOverEvent instance = new DbLoaderOverEvent();

	public static void fireEvent() {
		instance.fireEventHandler();
	}
}
