package org.qiunet.cfg.convert.xd;

import org.qiunet.cfg.convert.ICfgTypeConvert;

import java.io.DataInputStream;

public class CfgDoubleConvert implements ICfgTypeConvert<Double, DataInputStream> {

	@Override
	public Double returnObject(String fieldName, DataInputStream dis) throws Exception {
		return dis.readDouble();
	}
}
