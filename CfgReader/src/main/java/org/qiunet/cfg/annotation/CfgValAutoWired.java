package org.qiunet.cfg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * Key val注解
 * 根绝key 注入对应的val值.
 *
 * @author qiunet
 * 2020-09-18 17:37
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CfgValAutoWired {
	/**
	 * 配置的key
	 * @return
	 */
	String key() default "";
}
