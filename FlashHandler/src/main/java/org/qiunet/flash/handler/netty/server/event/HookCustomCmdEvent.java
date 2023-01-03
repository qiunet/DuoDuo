package org.qiunet.flash.handler.netty.server.event;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * Hook 自定义消息事件
 * @author qiunet
 * 2023/1/4 13:47
 */
public class HookCustomCmdEvent implements IListenerEvent {
	/**
	 * 对应的消息
	 */
	private String msg;

	public static HookCustomCmdEvent valueOf(String msg){
		HookCustomCmdEvent data = new HookCustomCmdEvent();
	    data.msg = msg;
		return data;
	}

	public String getMsg() {
		return msg;
	}
}
