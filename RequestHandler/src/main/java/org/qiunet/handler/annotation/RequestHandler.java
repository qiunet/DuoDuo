package org.qiunet.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记请求处理类. 指定id 用来分发请求. 解耦处理代码和配置.
 * @author qiunet
 *         Created on 17/1/24 09:32.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestHandler {
	/***
	 * request Command Id
	 * @return
	 */
	short ID();
}
