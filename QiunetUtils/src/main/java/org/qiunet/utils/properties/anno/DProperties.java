package org.qiunet.utils.properties.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 对一个properties的注解.
 *
 * @author qiunet
 * 2020-09-18 10:06
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DProperties {
	/**
	 * properties 文件名.
	 * 需要包含后缀以及在classpath目录的相对路径
	 * @return
	 */
	String value();
}
