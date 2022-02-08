package org.qiunet.flash.handler.util.proto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 21:05
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipProtoGenerator {
}
