package org.qiunet.quartz;

import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;
import org.qiunet.utils.classScanner.Singleton;

/***
 * 扫描annotation
 */
@Singleton
public class CronAnnotationScannerHandler implements IApplicationContextAware {
	@Override
	public void setApplicationContext(IApplicationContext context) {
		context.getMethodsAnnotatedWith(CronSchedule.class).stream()
			.map(m -> {
				CronSchedule cron = m.getAnnotation(CronSchedule.class);
				return new CommonCronJob(cron.value(), cron.warnExecMillis(), m, context.getInstanceOfClass(m.getDeclaringClass()));
				}
			).forEach(QuartzSchedule.getInstance()::addJob);
	}
}
