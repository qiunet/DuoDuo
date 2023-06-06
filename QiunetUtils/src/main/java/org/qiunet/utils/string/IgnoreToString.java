package org.qiunet.utils.string;

import java.lang.annotation.*;

/***
 * ToString  忽略
 * @author qiunet
 * 2023/6/5 16:42
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IgnoreToString {
}
