package org.qiunet.utils.properties.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 注解一个字段. 表示从properties 读取
 *
 * @author qiunet
 * 2020-09-18 10:12
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DPropertiesValue {
	/**
	 * 该字段在properties的对应key
	 * @return
	 */
	String value();

	/**
	 * 默认值. 如果没定义. 默认值.
	 * @return
	 */
	String defaultVal() default "";

	/**
	 * 如果类没有指定{@link DProperties} 则需要指定这个properties name
	 * @return
	 */
	String propertiesName() default "";
}
