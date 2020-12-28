package org.qiunet.utils.scanner.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * DuoDuo部分逻辑没有实现业务里面的逻辑. 但是需要接口注入.
 * 所以忽略 没有实现类 报错.
 *
 * @author qiunet
 * 2020-12-28 16:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreEmptyWired {
}
