package org.qiunet.cfg.annotation.support;

import org.qiunet.cfg.convert.ICfgTypeConvert;
import org.qiunet.cfg.manager.CfgTypeConvertManager;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;

import java.lang.reflect.Modifier;
import java.util.Set;

public class CfgTypeConvertScannerHandler implements IApplicationContextAware {
	@Override
	public void setApplicationContext(IApplicationContext context) {
		Set<Class<? extends ICfgTypeConvert>> set = context.getSubTypesOf(ICfgTypeConvert.class);

		set.stream().filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
			.forEach(clazz -> CfgTypeConvertManager.getInstance().addConvertClass(clazz));
	}
}
