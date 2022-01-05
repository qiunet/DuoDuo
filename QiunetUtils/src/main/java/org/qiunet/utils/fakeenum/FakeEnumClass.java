package org.qiunet.utils.fakeenum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 假枚举 类 注解
 * @author qiunet
 * 2022/1/3 09:17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FakeEnumClass {
}
