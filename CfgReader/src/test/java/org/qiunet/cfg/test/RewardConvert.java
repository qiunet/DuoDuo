package org.qiunet.cfg.test;

import org.qiunet.cfg.convert.BaseObjConvert;

public class RewardConvert extends BaseObjConvert<RewardData> {
	@Override
	protected RewardData fromString0(String str) {
		return new RewardData(str);
	}

	@Override
	public boolean canConvert(Class type) {
		return type == RewardData.class;
	}
}
