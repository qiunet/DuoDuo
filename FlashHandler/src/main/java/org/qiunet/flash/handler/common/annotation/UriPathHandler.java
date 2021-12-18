package org.qiunet.flash.handler.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非游戏的请求 可以json 什么的
 * Created by qiunet.
 * 17/11/23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UriPathHandler {
	/***
	 * uriPath
	 * 仅http使用.
	 * @return
	 */
	String value();
	/**
	 * 解析post里面的参数.
	 * 内容格式: p1=val1&p2=val2
	 * @return 是否解析
	 */
	boolean post_params() default false;
}
