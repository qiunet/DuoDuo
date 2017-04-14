package org.qiunet.handler.annotation.support;

import org.qiunet.handler.gamedata.GameSettingManagers;
import org.qiunet.handler.gamedata.IGameDataManager;
import org.qiunet.handler.annotation.GameDataSetting;
import org.qiunet.utils.classScanner.IScannerHandler;

import java.lang.reflect.Constructor;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
public class GameDataScannerHandler implements IScannerHandler {
	@Override
	public boolean matchClazz(Class clazz) {
		return clazz.getAnnotation(GameDataSetting.class) != null;
	}
	
	@Override
	public void handler(Class<?> clazz) {
		GameDataSetting setting = clazz.getAnnotation(GameDataSetting.class);
		try {
			Constructor<IGameDataManager> constructor = (Constructor<IGameDataManager>) clazz.getConstructor(null);
			if (!constructor.isAccessible()) constructor.setAccessible(true);
			IGameDataManager manager = constructor.newInstance();
			
			GameSettingManagers.getInstance().addDataSettingManager(manager , setting.order());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
