package org.qiunet.cfg.annotation.support;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfgManager;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
public class CfgScannerHandler implements IApplicationContextAware {

	@Override
	public void setApplicationContext(IApplicationContext context) {
		Set<Class<? extends ICfgManager>> set = context.getSubTypesOf(ICfgManager.class);
		for (Class<? extends ICfgManager> aClass : set) {
			if (Modifier.isAbstract(aClass.getModifiers())) continue;

			this.handler(aClass);
		}
	}


	private void handler(Class<? extends ICfgManager> clazz) {
		Cfg setting = clazz.getAnnotation(Cfg.class);
		try {
			Constructor<? extends ICfgManager> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			ICfgManager manager = constructor.newInstance();

			CfgManagers.getInstance().addDataSettingManager(manager , setting == null ? 0 : setting.order());
		} catch (Exception e) {
		}
	}
}
