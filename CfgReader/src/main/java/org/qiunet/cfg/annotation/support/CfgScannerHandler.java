package org.qiunet.cfg.annotation.support;

import org.qiunet.cfg.annotation.Cfg;
import org.qiunet.cfg.base.ICfgManager;
import org.qiunet.cfg.manager.CfgManagers;
import org.qiunet.utils.classScanner.IScannerHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
public class CfgScannerHandler implements IScannerHandler {
	@Override
	public boolean matchClazz(Class clazz) {
		return ! Modifier.isAbstract(clazz.getModifiers())
				&& ICfgManager.class.isAssignableFrom(clazz);
	}

	@Override
	public void handler(Class<?> clazz) {
		Cfg setting = clazz.getAnnotation(Cfg.class);
		try {
			Constructor<ICfgManager> constructor = (Constructor<ICfgManager>) clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			ICfgManager manager = constructor.newInstance();

			CfgManagers.getInstance().addDataSettingManager(manager , setting == null ? 0 : setting.order());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
