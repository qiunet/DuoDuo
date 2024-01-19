package org.qiunet.cross.actor.event;

import org.qiunet.cross.event.CrossPlayerEvent;

/***
 * 跨服对象鉴权成功事件
 *
 * @author qiunet
 * 2021/11/20 15:59
 */
public class CrossPlayerAuthSuccessEvent extends CrossPlayerEvent {

	public static CrossPlayerAuthSuccessEvent valueOf(){
		return new CrossPlayerAuthSuccessEvent();
	}
}
