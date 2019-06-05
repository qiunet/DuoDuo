package org.qiunet.cfg.annotation.support;

import org.qiunet.cfg.annotation.GameProperties;
import org.qiunet.cfg.manager.GameCfgManagers;
import org.qiunet.utils.classScanner.IScannerHandler;
import org.qiunet.utils.properties.LoaderProperties;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * @author qiunet
 *         Created on 17/2/9 15:43.
 */
public class PropertiesScannerHandler implements IScannerHandler{

	@Override
	public boolean matchClazz(Class clazz) {
		return ! Modifier.isAbstract(clazz.getModifiers())
				&& LoaderProperties.class.isAssignableFrom(clazz);
	}

	@Override
	public void handler(Class<?> clazz) {
		GameProperties setting = clazz.getAnnotation(GameProperties.class);
		try {
			Constructor<LoaderProperties> constructor = (Constructor<LoaderProperties>) clazz.getDeclaredConstructor(null);
			if (!constructor.isAccessible()) constructor.setAccessible(true);
			LoaderProperties properties = constructor.newInstance();
			GameCfgManagers.getInstance().addPropertySetting(properties , setting == null ? 0 : setting.order());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
