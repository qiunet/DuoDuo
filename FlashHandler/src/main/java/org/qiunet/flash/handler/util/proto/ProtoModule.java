package org.qiunet.flash.handler.util.proto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 对proto文件夹的注解.
 * 表示proto属于哪个 proto模块.
 *
 * @author qiunet
 * 2022/2/10 13:52
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoModule {
	/**
	 * proto模块 文件名
	 * @return
	 */
	String value();
}
