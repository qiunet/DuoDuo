package org.qiunet.cfg.annotation.support;

import org.qiunet.cfg.convert.ICfgTypeConvert;
import org.qiunet.cfg.manager.CfgTypeConvertManager;
import org.qiunet.utils.classScanner.IScannerHandler;

public class CfgTypeConvertScannerHandler implements IScannerHandler {
	@Override
	public boolean matchClazz(Class clazz) {
		return clazz.isAssignableFrom(ICfgTypeConvert.class);
	}

	@Override
	public void handler(Class<?> clazz) {
		CfgTypeConvertManager.getInstance().addConvertClass((Class<? extends ICfgTypeConvert>) clazz);
	}
}
