package org.qiunet.cross.actor.data;

import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;

/***
 * cross data 更新事件
 * @author qiunet
 * 2022/9/17 14:29
 */
public class _CrossDataNeedUpdateEvent extends BasePlayerEventData {
	/**
	 * cross data key
	 */
	private String key;
	/**
	 * 数据
	 */
	private String json;

	public static _CrossDataNeedUpdateEvent valueOf(String key, String json){
		_CrossDataNeedUpdateEvent data = new _CrossDataNeedUpdateEvent();
	    data.json = json;
		data.key = key;
		return data;
	}

	public String getKey() {
		return key;
	}

	public String getJson() {
		return json;
	}
}
