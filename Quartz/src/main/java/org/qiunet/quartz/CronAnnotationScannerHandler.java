package org.qiunet.quartz;

import org.qiunet.utils.classScanner.IApplicationContext;
import org.qiunet.utils.classScanner.IApplicationContextAware;

/***
 * 扫描annotation
 */
public class CronAnnotationScannerHandler implements IApplicationContextAware {
	@Override
	public void setApplicationContext(IApplicationContext context) {
		context.getMethodsAnnotatedWith(CronSchedule.class).stream()
			.map(m -> {
				CronSchedule cron = m.getAnnotation(CronSchedule.class);
				return new CommonCronJob(CronConfigProperties.getInstance().getProperty(cron.value()),
						cron.logExecInfo(), m, context.getInstanceOfClass(m.getDeclaringClass()));
				}
			).forEach(QuartzSchedule.getInstance()::addJob);
	}
}
