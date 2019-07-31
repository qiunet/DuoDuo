package org.qiunet.entity2table.start;

import org.qiunet.entity2table.controller.CreateTableController;
import org.qiunet.utils.listener.EventHandler;
import org.qiunet.utils.listener.IEventData;
import org.qiunet.utils.listener.IEventListener;
import org.qiunet.utils.listener.event_data.ServerStartEventData;

/**
 * Created by zhengj
 * Date: 2019/7/31.
 * Time: 10:56.
 * To change this template use File | Settings | File Templates.
 */
public class Entity2TableStartHandler implements IEventListener {
	@Override
	@EventHandler(value = ServerStartEventData.class, weight = Integer.MAX_VALUE - 1)
	public void eventHandler(IEventData eventData) {
		CreateTableController.getInstance().start();
	}
}
