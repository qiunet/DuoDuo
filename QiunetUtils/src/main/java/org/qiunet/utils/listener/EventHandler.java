package org.qiunet.utils.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给 {@link IEventListener#eventHandler(IEventData)}方法注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
	/***
	 * 这里应该填枚举值. 但是java限制很多
	 * 只能使用常量.
	 * @return
	 */
	Class<? extends IEventData>[] value();
}
