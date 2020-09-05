package org.qiunet.quartz;


import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;

/***
 * 扫描annotation
 */
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
