package org.qiunet.flash.handler.util.proto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 协议的替身.
 * 可以指定用一个其它协议来解析该行数据对象.
 *
 * 比如我们下发缓存的bytes, 客户端直接解析成对应的对象. 避免解析两次.
 *
 * @author qiunet
 * 2023/2/28 11:01
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoSubstitute {
	/**
	 * 替身的class
	 * @return class
	 */
	Class<?> value();
}
