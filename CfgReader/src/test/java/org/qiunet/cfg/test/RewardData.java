package org.qiunet.cfg.test;

import org.qiunet.utils.string.StringUtil;

public class RewardData {
	private final String [] reward;
	public RewardData(String reward) {
		this.reward = StringUtil.split(reward, ";");
	}

	public int size(){
		return reward.length;
	}
}
