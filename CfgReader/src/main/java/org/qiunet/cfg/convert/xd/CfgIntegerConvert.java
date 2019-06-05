package org.qiunet.cfg.convert.xd;

import org.qiunet.cfg.convert.ICfgTypeConvert;

import java.io.DataInputStream;

public class CfgIntegerConvert implements ICfgTypeConvert<Integer, DataInputStream> {

	@Override
	public Integer returnObject(String fieldName, DataInputStream dis) throws Exception {
		return dis.readInt();
	}
}
