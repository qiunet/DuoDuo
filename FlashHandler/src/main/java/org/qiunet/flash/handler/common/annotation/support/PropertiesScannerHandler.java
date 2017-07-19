package org.qiunet.flash.handler.common.annotation.support;

import org.qiunet.flash.handler.common.annotation.GameProperties;
import org.qiunet.flash.handler.gamecfg.GameCfgManagers;
import org.qiunet.utils.classScanner.IScannerHandler;
import org.qiunet.utils.properties.LoaderProperties;

import java.lang.reflect.Constructor;

/**
 * @author qiunet
 *         Created on 17/2/9 15:43.
 */
public class PropertiesScannerHandler implements IScannerHandler{

	@Override
	public boolean matchClazz(Class clazz) {
		return clazz.getAnnotation(GameProperties.class) != null;
	}

	@Override
	public void handler(Class<?> clazz) {
		GameProperties setting = clazz.getAnnotation(GameProperties.class);
		try {
			Constructor<LoaderProperties> constructor = (Constructor<LoaderProperties>) clazz.getConstructor(null);
			if (!constructor.isAccessible()) constructor.setAccessible(true);
			LoaderProperties properties = constructor.newInstance();

			GameCfgManagers.getInstance().addPropertySetting(properties , setting.order());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
