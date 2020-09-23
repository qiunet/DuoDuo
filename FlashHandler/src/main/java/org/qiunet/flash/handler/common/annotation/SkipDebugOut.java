package org.qiunet.flash.handler.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 跳过调试输出内容打印.
 *
 * @author qiunet
 * 2020-09-23 15:17
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipDebugOut {
}
