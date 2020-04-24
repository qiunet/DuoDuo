package org.qiunet.cfg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 注解配置Wrapper字段.
 * 系统自动注入实例
 *
 * @author qiunet
 * 2020-04-23 21:37
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CfgWrapperAutoWired {
}
