package org.qiunet.cfg.test;

import org.qiunet.cfg.convert.ICfgTypeConvert;

import java.io.DataInputStream;

public class RewardCfgTypeConvert implements ICfgTypeConvert<RewardData, DataInputStream> {
	@Override
	public RewardData returnObject(String fieldName, DataInputStream dataInputStream) throws Exception {
		return new RewardData(dataInputStream.readUTF());
	}
}
