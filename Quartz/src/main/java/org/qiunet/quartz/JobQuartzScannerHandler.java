package org.qiunet.quartz;

import org.qiunet.utils.classScanner.IScannerHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * 启动时候.把manager 添加到GameSettingManagers
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
public class JobQuartzScannerHandler implements IScannerHandler {
	@Override
	public boolean matchClazz(Class clazz) {
		return ! Modifier.isAbstract(clazz.getModifiers())
				&& IJob.class.isAssignableFrom(clazz);
	}

	@Override
	public void handler(Class<?> clazz) {
		try {
			Constructor<IJob> constructor = (Constructor<IJob>) clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			IJob job = constructor.newInstance();

			QuartzSchedule.getInstance().addJob(job);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
