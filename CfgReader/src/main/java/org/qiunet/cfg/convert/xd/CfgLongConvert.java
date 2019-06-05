package org.qiunet.cfg.convert.xd;

import org.qiunet.cfg.convert.ICfgTypeConvert;

import java.io.DataInputStream;

public class CfgLongConvert implements ICfgTypeConvert<Long, DataInputStream> {

	@Override
	public Long returnObject(String fieldName, DataInputStream dis) throws Exception {
		return dis.readLong();
	}
}
