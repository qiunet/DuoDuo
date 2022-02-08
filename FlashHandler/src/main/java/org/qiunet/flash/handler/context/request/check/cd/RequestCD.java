package org.qiunet.flash.handler.context.request.check.cd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/***
 * 监视request的请求cd
 *
 * @author qiunet
 * 2022/2/8 10:15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestCD {
	/**
	 * cd 数值.
	 * 默认毫秒
	 * @return
	 */
	int value();
	/**
	 * cd数值的单位
	 * @return
	 */
	TimeUnit unit() default TimeUnit.MILLISECONDS;
	/**
	 * cd 时间内允许次数
	 * @return
	 */
	int limitCount() default 1;
}
