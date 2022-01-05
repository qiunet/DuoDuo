package org.qiunet.flash.handler.context.request.param.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 数值参数检查
 *
 * @author qiunet
 * 2022/1/5 17:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberParam {
	/**
	 * 自定义最小值
	 * @return
	 */
	long min() default 0;
	/**
	 * 自定义最大值
	 * @return
	 */
	long max() default 0;
	/**
	 * 自定义最小值 从key val 表读取
	 * 优先级 比{@link #min()} 高
	 * @return
	 */
	String minKey() default "";
	/**
	 * 自定义最大值 从key val 表读取
	 * 优先级 比{@link #max()} 高
	 * @return
	 */
	String maxKey() default "";
}
