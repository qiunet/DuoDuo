package org.qiunet.cfg.convert.xd;

import org.qiunet.cfg.convert.ICfgTypeConvert;

import java.io.DataInputStream;

public class CfgBooleanConvert implements ICfgTypeConvert<Boolean, DataInputStream> {

	@Override
	public Boolean returnObject(String fieldName, DataInputStream dis) throws Exception {
		int temp = dis.readInt();
		return temp == 1;
	}
}
