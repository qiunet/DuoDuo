package org.qiunet.test.response.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解. 对响应处理类的
 * Created by qiunet.
 * 17/12/6
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Response {
	/**
	 * 响应的id
	 * @return
	 */
	int ID();
}
