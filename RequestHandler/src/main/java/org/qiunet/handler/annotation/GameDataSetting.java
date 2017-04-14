package org.qiunet.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 游戏设定数据处理类
 * @author qiunet
 *         Created on 17/2/9 12:11.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GameDataSetting {
	/***
	 * order越大, 执行越靠前
	 * @return
	 */
	int order() default 0;
}
