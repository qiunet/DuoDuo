package org.qiunet.utils.config.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 可以根据需要注入对应的
 * {@link org.qiunet.utils.config.conf.DHocon}
 * {@link org.qiunet.utils.config.properties.DProperties}
 * {@link org.qiunet.utils.data.IKeyValueData}
 *
 * @author qiunet
 * 2020-12-25 09:47
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DConfigInstance {
	/**
	 * 文件名称
	 * @return
	 */
	String value();
}
