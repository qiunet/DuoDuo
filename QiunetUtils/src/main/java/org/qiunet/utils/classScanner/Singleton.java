package org.qiunet.utils.classScanner;

import java.lang.annotation.*;

/***
 * 扫描器获取实例的类必须是单例.
 * 必须有该注解.
 *
 * @author qiunet
 * 2020-04-23 20:52
 **/
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
}
