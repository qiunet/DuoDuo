package org.qiunet.utils.config.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 注解一个字段. 表示从 配置文件(properties conf) 读取
 *
 * @author qiunet
 * 2020-09-18 10:12
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DConfigValue {
	/**
	 * 前缀 key
	 * @return
	 */
	String prefixKey() default "";
	/**
	 * 后缀 key
	 * @return
	 */
	String postfixKey() default "";

	/**
	 * 该字段在(配置文件[conf, properties])的对应key
	 * @return
	 */
	String value() default "";

	/**
	 * 默认值. 如果没定义. 默认值.
	 * @return
	 */
	String defaultVal() default "-";

	/**
	 * 如果类没有指定{@link DConfig} 则需要指定这个文件名: file name
	 * @return
	 */
	String configName() default "";
}
