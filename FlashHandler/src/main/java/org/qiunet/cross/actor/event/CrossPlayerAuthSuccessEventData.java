package org.qiunet.cross.actor.event;

import org.qiunet.cross.event.BaseCrossPlayerEventData;

/***
 * 跨服对象鉴权成功事件
 *
 * @author qiunet
 * 2021/11/20 15:59
 */
public class CrossPlayerAuthSuccessEventData extends BaseCrossPlayerEventData {

	public static CrossPlayerAuthSuccessEventData valueOf(){
		return new CrossPlayerAuthSuccessEventData();
	}
}
