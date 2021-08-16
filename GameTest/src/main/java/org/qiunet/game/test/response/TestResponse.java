package org.qiunet.game.test.response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 响应的注解
 *
 * @author qiunet
 * 2021-07-07 15:14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestResponse {
	/**
	 * 响应ID
	 * @return
	 */
	int value();
}
