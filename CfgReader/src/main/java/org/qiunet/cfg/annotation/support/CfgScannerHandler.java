package org.qiunet.cfg.annotation.support;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfg;
import org.qiunet.cfg.wrapper.CfgType;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;

import java.util.Set;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
public class CfgScannerHandler implements IApplicationContextAware {
	private IApplicationContext context;
	@Override
	public void setApplicationContext(IApplicationContext context) {
		this.context = context;

		Set<Class<?>> classSet = context.getTypesAnnotatedWith(Cfg.class);
		for (Class<?> aClass : classSet) {
			CfgType.createCfgWrapper((Class<? extends ICfg>) aClass);
		}
	}
}
