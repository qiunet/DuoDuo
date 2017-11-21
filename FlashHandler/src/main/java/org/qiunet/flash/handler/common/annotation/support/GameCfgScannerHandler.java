package org.qiunet.flash.handler.common.annotation.support;

import org.qiunet.flash.handler.common.annotation.GameCfg;
import org.qiunet.flash.handler.gamecfg.GameCfgManagers;
import org.qiunet.flash.handler.gamecfg.IGameCfgManager;
import org.qiunet.utils.classScanner.IScannerHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
public class GameCfgScannerHandler implements IScannerHandler {
	@Override
	public boolean matchClazz(Class clazz) {
		return ! Modifier.isAbstract(clazz.getModifiers())
				&& IGameCfgManager.class.isAssignableFrom(clazz);
	}

	@Override
	public void handler(Class<?> clazz) {
		GameCfg setting = clazz.getAnnotation(GameCfg.class);
		try {
			Constructor<IGameCfgManager> constructor = (Constructor<IGameCfgManager>) clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			IGameCfgManager manager = constructor.newInstance();

			GameCfgManagers.getInstance().addDataSettingManager(manager , setting == null ? 0 : setting.order());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
