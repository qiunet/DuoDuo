package org.qiunet.quartz;


import org.qiunet.utils.args.ArgsContainer;
import org.qiunet.utils.scanner.IApplicationContext;
import org.qiunet.utils.scanner.IApplicationContextAware;
import org.qiunet.utils.scanner.ScannerType;

/***
 * 扫描annotation
 */
enum CronAnnotationScannerHandler implements IApplicationContextAware {
	instance;

	@Override
	public void setApplicationContext(IApplicationContext context, ArgsContainer argsContainer) {
		context.getMethodsAnnotatedWith(CronSchedule.class).stream()
			.map(m -> {
				CronSchedule cron = m.getAnnotation(CronSchedule.class);
				return new CommonCronJob(cron.value(), cron.warnExecMillis(), m, context.getInstanceOfClass(m.getDeclaringClass()));
				}
			).forEach(QuartzSchedule.getInstance()::addJob);
	}

	@Override
	public ScannerType scannerType() {
		return ScannerType.CRON;
	}
}
