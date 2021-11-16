package org.qiunet.cfg.test;

import org.qiunet.cfg.convert.BaseObjConvert;

import java.lang.reflect.Field;

public class RewardConvert extends BaseObjConvert<RewardData> {
	@Override
	public RewardData fromString(Field field, String str) {
		return new RewardData(str);
	}
}
