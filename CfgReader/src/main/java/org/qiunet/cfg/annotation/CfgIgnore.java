package org.qiunet.cfg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 *
 *
 * qiunet
 * 2019-08-22 20:11
 ***/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CfgIgnore {
}
