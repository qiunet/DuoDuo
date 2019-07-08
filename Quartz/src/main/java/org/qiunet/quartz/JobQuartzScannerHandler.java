package org.qiunet.quartz;

import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 *
 * @author qiunet
 *         Created on 17/2/9 15:25.
 */
public class JobQuartzScannerHandler implements IApplicationContextAware {

	@Override
	public void setApplicationContext(IApplicationContext context) {
		Set<Class<? extends IJob>> set = context.getSubTypesOf(IJob.class);
		for (Class<? extends IJob> clazz : set) {
			if (Modifier.isAbstract(clazz.getModifiers()) ||
				CommonCronJob.class == clazz
			) continue;

			QuartzSchedule.getInstance().addJob((IJob) context.getInstanceOfClass(clazz));
		}
	}
}
