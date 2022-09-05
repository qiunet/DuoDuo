package org.qiunet.cfg.annotation;

import org.qiunet.cfg.base.ICfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 配置加载完成后, 通知manager. 如果有.
 *
 * @author qiunet
 * 2021-01-27 20:15
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CfgLoadOver {}
