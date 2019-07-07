package org.qiunet.quartz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 可以使用该注解注解无参方法.
 *  会按照value的CronExpression触发方法调用
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CronSchedule {
	/***
	 * quartz 表达式
	 *
	 * @return
	 */
	String value();
	/**
	 * 打印执行信息
	 * @return
	 */
	boolean logExecInfo() default true;
}


