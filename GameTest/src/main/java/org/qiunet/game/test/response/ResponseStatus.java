package org.qiunet.game.test.response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 响应的status 处理
 *
 * @author qiunet
 * 2022/8/18 14:23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseStatus {
	/**
	 * 需要处理的status 数组
	 *
	 * @return
	 */
	int [] value();
}
