package org.qiunet.cfg.convert.xd;

import org.qiunet.cfg.convert.ICfgTypeConvert;

import java.io.DataInputStream;

public class CfgStringConvert implements ICfgTypeConvert<String, DataInputStream> {

	@Override
	public String returnObject(String fieldName, DataInputStream dis) throws Exception {
		return dis.readUTF();
	}
}
