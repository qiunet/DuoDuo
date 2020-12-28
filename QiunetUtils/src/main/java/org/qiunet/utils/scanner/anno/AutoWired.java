package org.qiunet.utils.scanner.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 跟spring 类似. 注入对象
 * 如果注解的字段对象为接口 或者抽象类. 则必须只能有一个实现类.
 * 被注入的对象需要保证是单例. 否则会有不可预测问题.
 * @author qiunet
 * 2020-12-28 12:19
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoWired {}
