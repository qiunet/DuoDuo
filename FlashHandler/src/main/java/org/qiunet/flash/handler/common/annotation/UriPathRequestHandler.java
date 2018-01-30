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
public @interface UriPathRequestHandler {
	/***
	 * uriPath
	 * 仅http使用.
	 * @return
	 */
	String uriPath();
}
