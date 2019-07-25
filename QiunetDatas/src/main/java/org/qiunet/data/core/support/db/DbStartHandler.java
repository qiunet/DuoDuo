package org.qiunet.data.core.support.db;

import org.qiunet.utils.listener.EventHandler;
import org.qiunet.utils.listener.IEventData;
import org.qiunet.utils.listener.IEventListener;
import org.qiunet.utils.listener.event_data.ServerStartEventData;

/***
 * db 启动监听处理
 */
class DbStartHandler implements IEventListener {

	@Override
	@EventHandler(value = ServerStartEventData.class, weight = Integer.MAX_VALUE)
	public void eventHandler(IEventData eventData) {
		DbLoader.getInstance();
	}
}
