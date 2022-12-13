package org.qiunet.function.reward;

import org.qiunet.utils.listener.event.IListenerEvent;

/***
 * 获得奖励事件
 * 仅仅获得奖励本身
 *
 * @author qiunet
 * 2021-01-05 20:43
 */
public class GainRewardEvent implements IListenerEvent {
	/**
	 * 奖励的上下文
	 */
	private RewardContext context;

	public static GainRewardEvent valueOf(RewardContext context){
		GainRewardEvent data = new GainRewardEvent();
	    data.context = context;
		return data;
	}

	public RewardContext getContext() {
		return context;
	}
}
