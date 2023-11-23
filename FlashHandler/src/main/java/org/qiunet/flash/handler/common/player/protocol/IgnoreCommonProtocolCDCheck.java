package org.qiunet.flash.handler.common.player.protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不检查 通用协议cd
 * @author qiunet
 * 2023/11/21 21:19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreCommonProtocolCDCheck {
}
